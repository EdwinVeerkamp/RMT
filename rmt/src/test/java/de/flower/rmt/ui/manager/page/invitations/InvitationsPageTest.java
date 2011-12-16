package de.flower.rmt.ui.manager.page.invitations;

import de.flower.rmt.model.event.Event;
import de.flower.rmt.test.AbstractWicketIntegrationTests;
import de.flower.rmt.ui.model.EventModel;
import org.testng.annotations.Test;

/**
 * @author flowerrrr
 */

public class InvitationsPageTest extends AbstractWicketIntegrationTests {

    @Test
    public void testRender() {
        Event event = testData.createEventWithResponses();
        wicketTester.startPage(new InvitationsPage(new EventModel(event)));
        wicketTester.dumpPage();
    }
}