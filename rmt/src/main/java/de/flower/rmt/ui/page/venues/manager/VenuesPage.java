package de.flower.rmt.ui.page.venues.manager;

import de.flower.rmt.ui.page.base.manager.ManagerBasePage;
import de.flower.rmt.ui.page.base.manager.NavigationPanel;

/**
 * @author flowerrrr
 */
public class VenuesPage extends ManagerBasePage {

    public VenuesPage() {

        setHeading("manager.venues.heading", null);
        addMainPanel(new VenueMainPanel());
        addSecondaryPanel(new VenuesSecondaryPanel());
    }

    @Override
    public String getActiveTopBarItem() {
        return NavigationPanel.VENUES;
    }
}