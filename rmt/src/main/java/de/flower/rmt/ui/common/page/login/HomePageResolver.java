package de.flower.rmt.ui.common.page.login;

import de.flower.rmt.ui.app.Authentication;
import de.flower.rmt.ui.common.page.HomePage;
import de.flower.rmt.ui.manager.ManagerHomePage;
import de.flower.rmt.ui.player.PlayerHomePage;
import org.apache.wicket.request.component.IRequestablePage;



/**
 * @author oblume
 */
public class HomePageResolver {

    /**
     * Determine if user is manager or player and redirect to appropriate home page.
     * @param application
     */
    public static Class<? extends IRequestablePage> getHomePage() {
        // get roles
        if (Authentication.getRoles().hasRole(Role.MANAGER.getRoleName())) {
            return ManagerHomePage.class;
        } else if (Authentication.getRoles().hasRole(Role.PLAYER.getRoleName())) {
            return PlayerHomePage.class;
        } else {
            return HomePage.class;
        }

        // map to homepage
    }
}
