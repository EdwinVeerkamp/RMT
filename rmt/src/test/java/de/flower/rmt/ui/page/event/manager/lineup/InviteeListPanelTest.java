package de.flower.rmt.ui.page.event.manager.lineup;

import de.flower.rmt.model.db.entity.Lineup;
import de.flower.rmt.model.db.entity.event.Event;
import de.flower.rmt.test.AbstractRMTWicketIntegrationTests;
import de.flower.rmt.ui.model.EventModel;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */
public class InviteeListPanelTest extends AbstractRMTWicketIntegrationTests {

    @Test
    public void testRender() {
        Event event = testData.createEventWithResponses();
        Lineup lineup = testData.createLineup(event);
        wicketTester.startComponentInPage(new InviteeListPanel(new EventModel(event)));
        wicketTester.dumpComponentWithPage();
    }
}
