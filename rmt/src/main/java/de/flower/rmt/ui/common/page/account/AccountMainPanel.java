package de.flower.rmt.ui.common.page.account;

import de.flower.rmt.model.User;
import de.flower.rmt.ui.common.panel.AbstractAjaxTabbedPanel;
import de.flower.rmt.ui.model.UserModel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author flowerrrr
 */
public class AccountMainPanel extends AbstractAjaxTabbedPanel<User> {

    public static final int PASSWORD_RESET_PANEL_INDEX = 1;

    public AccountMainPanel() {
    }

    @Override
    protected void addTabs(final List<ITab> tabs) {
        final IModel<User> model = new UserModel(getUserDetails().getUser());
        tabs.add(new AbstractTab(new ResourceModel("player.account.general")) {
            @Override
            public Panel getPanel(String panelId) {
                return new AccountGeneralPanel(panelId, model);
            }
        });
        tabs.add(new AbstractTab(new ResourceModel("player.account.password")) {
            @Override
            public Panel getPanel(String panelId) {
                return new AccountPasswordPanel(panelId, model);
            }
        });

    }
}
