package de.flower.rmt.ui.common.page.event;

import de.flower.rmt.model.event.Event;
import de.flower.rmt.model.event.EventType;
import de.flower.rmt.service.IEventManager;
import de.flower.rmt.ui.common.panel.BasePanel;
import de.flower.rmt.ui.manager.page.event.EventEditPage;
import de.flower.rmt.ui.model.ModelFactory;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author flowerrrr
 */
public class EventDetailsPanel extends BasePanel<Event> {

    @SpringBean
    private IEventManager eventManager;

    public EventDetailsPanel(final IModel<Event> model) {
        setDefaultModel(new CompoundPropertyModel<Object>(ModelFactory.eventModelWithAllAssociations(model.getObject())));

        add(new Label("team.name"));
        add(DateLabel.forDateStyle("date", "S-"));
        add(DateLabel.forDateStyle("timeAsDate", "-S"));
        add(new Label("type", new ResourceModel(EventType.from(model.getObject()).getResourceKey())));
        add(new Label("opponent.name") {
            @Override
            public boolean isVisible() {
                return EventType.isMatch(model.getObject());
            }
        });
        add(new Label("venue.name"));
        add(new Label("summary"));
        add(new Label("comment"));

        add(new Link("editButton") {

            @Override
            public void onClick() {
                setResponsePage(new EventEditPage(model));
            }

            @Override
            public boolean isVisible() {
                return getUserDetails().isManager();
            }
        });
    }
}
