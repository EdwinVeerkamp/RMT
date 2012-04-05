package de.flower.rmt.ui.manager.page.event;

import de.flower.common.ui.ajax.markup.html.tab.AbstractAjaxTabbedPanel;
import de.flower.rmt.model.event.Event;
import de.flower.rmt.ui.manager.page.event.edit.EventEditPanel;
import de.flower.rmt.ui.manager.page.event.invitations.InvitationListPanel;
import de.flower.rmt.ui.manager.page.event.invitees.InviteeListPanel;
import de.flower.rmt.ui.manager.page.event.notification.NotificationPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author flowerrrr
 */
public class EventTabPanel extends AbstractAjaxTabbedPanel<Event> {

    public static final int EVENT_EDIT_PANEL_INDEX = 0;

    public static final int INVITEES_PANEL_INDEX = 1;

    public static final int NOTIFICATION_PANEL_INDEX = 2;

    public static final int INVITATIONS_PANEL_INDEX = 3;

    public EventTabPanel(IModel<Event> model) {
        super(model);
    }

    @Override
    protected void addTabs(final List<ITab> tabs) {
        final IModel<Event> model = this.getModel();
        tabs.add(new AbstractTab(new ResourceModel("manager.event.tab.edit")) {
            @Override
            public Panel getPanel(String panelId) {
                return new EventEditPanel(panelId, model);
            }
        });
        tabs.add(new AbstractTab(new ResourceModel("manager.event.tab.participants")) {
            @Override
            public Panel getPanel(String panelId) {
                return new InviteeListPanel(panelId, model);
            }

            @Override
            public boolean isVisible() {
                return !model.getObject().isNew();
            }
        });
        tabs.add(new AbstractTab(new ResourceModel("manager.event.tab.notification")) {
            @Override
            public Panel getPanel(String panelId) {
                return new NotificationPanel(panelId, model);
            }

            @Override
            public boolean isVisible() {
                return !model.getObject().isNew();
            }
        });
        tabs.add(new AbstractTab(new ResourceModel("manager.event.tab.invitations")) {
            @Override
            public Panel getPanel(String panelId) {
                return new InvitationListPanel(panelId, model);
            }

            @Override
            public boolean isVisible() {
                return !model.getObject().isNew();
            }
        });
    }
}
