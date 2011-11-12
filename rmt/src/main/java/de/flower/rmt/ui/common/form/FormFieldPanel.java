package de.flower.rmt.ui.common.form;

import de.flower.common.ui.Css;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.validation.IValidator;

/**
 * @author flowerrrr
 */
public class FormFieldPanel extends Panel {

    // must match the wicket:for attribute of the label
    protected final static String ID = "input";

    private boolean isValidated = false;

    final FormComponent formComponent;

    public FormFieldPanel(String id, FormComponent fc) {
        super(id);
        setOutputMarkupId(true);

        formComponent = fc;
        add(formComponent);
        add(new FeedbackPanel("feedback", new ComponentFeedbackMessageFilter(formComponent)));

        formComponent.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                isValidated = true;
                target.add(FormFieldPanel.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                if (e != null) {
                    throw e;
                }
                isValidated = false;
                target.add(FormFieldPanel.this);
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                // to avoid triggering validation when input c is empty.
                // // TODO (flowerrrr - 24.09.11) - could be extended to avoid validation when c value has not changed
                return new AjaxCallDecorator() {
                    @Override
                    public CharSequence decorateScript(Component c, CharSequence script) {
                        return "if(jQuery.trim(this.value)=='')return false; " + script;
                    }
                };
            }
        });

        // set css class of border according to validation result.
        IModel<String> model = new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                if (formComponent.getFeedbackMessage() != null) {
                    return Css.ERROR;
                } else if (isValidated) {
                    return Css.VALID;
                } else {
                    return "";
                }

            }
        };
        add(new AttributeAppender("class", model, " "));

        // set model of form component. form.getForm not available at constructor time, so we have
        // to use wrapping model to defer lookup of form.
        IModel formModelWrapperModel = new AbstractReadOnlyModel() {

            @Override
            public Object getObject() {
                return formComponent.getForm().getModel();
            }
        };
        formComponent.setModel(new PropertyModel(formModelWrapperModel, this.getId()));
    }

    @Override
    public void onDetach() {
        // reset state of component.
        isValidated = false;
        super.onDetach();
    }



    /**
     * Do some initialization stuff that cannot be done inside constructor call.
     */
    @Override
    protected void onConfigure() {
        // retrieving the 'calling' markup cannot be done in constructor.
        String labelKey = getLabelKey();
        formComponent.setLabel(new ResourceModel(labelKey));

    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        // replace whatever tag the user has given.
        // user can still write <input wicket:id="name" /> and it will be replaced with a div tag.
        tag.setName("div");
        super.onComponentTag(tag);
    }


    public String getLabelKey() {
        String labelKey;
        final IMarkupFragment markup = getMarkup();
        final MarkupElement markupElement = markup.get(0);
        labelKey = ((ComponentTag) markupElement).getAttribute("labelKey");
        return labelKey;
    }

    public FormComponent getFormComponent() {
        return formComponent;
    }

    public void addValidator(final IValidator<?> validator) {
        formComponent.add(validator);
    }


}
