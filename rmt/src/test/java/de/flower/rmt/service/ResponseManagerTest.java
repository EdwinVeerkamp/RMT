package de.flower.rmt.service;

import de.flower.rmt.model.Invitation;
import de.flower.rmt.model.Player;
import de.flower.rmt.model.RSVPStatus;
import de.flower.rmt.model.event.Event;
import de.flower.rmt.test.AbstractIntegrationTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * @author flowerrrr
 */

public class ResponseManagerTest extends AbstractIntegrationTests {

    @Test
    public void testFindResponder() {
        Event event = testData.createEventWithoutResponses();
        List<Player> players = event.getTeam().getPlayers();
        assertTrue(invitationManager.findByEventAndStatus(event, RSVPStatus.ACCEPTED).isEmpty());
        Invitation invitation = responseManager.respond(event, players.get(0).getUser(), RSVPStatus.ACCEPTED, "some comment");
        List<Invitation> invitations = invitationManager.findByEventAndStatus(event, RSVPStatus.ACCEPTED);
        assertEquals(invitation, invitations.get(0));
        assertEquals((long) invitationManager.numByEventAndStatus(event, RSVPStatus.ACCEPTED), invitations.size());

        invitation = responseManager.respond(event, players.get(1).getUser(), RSVPStatus.ACCEPTED, "some comment");
        invitations = invitationManager.findByEventAndStatus(event, RSVPStatus.ACCEPTED);
        assertTrue(invitations.size() == 2);
        assertEquals((long) invitationManager.numByEventAndStatus(event, RSVPStatus.ACCEPTED), invitations.size());
    }

    @Test
    public void testRespond() {
        Event event = testData.createEventWithoutResponses();
        Player player = event.getTeam().getPlayers().get(0);
        String comment = "Comment #1";
        RSVPStatus status = RSVPStatus.ACCEPTED;
        // first initial invitation
        Invitation invitation = responseManager.respond(event, player.getUser(), status, comment);
        assertEquals(invitation.getStatus(), status);
        assertEquals(invitation.getComment(), comment);

        // update invitation for player
        comment = "Comment #2";
        status = RSVPStatus.DECLINED;
        invitation = responseManager.respond(event, player.getUser(), status, comment);
        assertEquals(invitation.getStatus(), status);
        assertEquals(invitation.getComment(), comment);
    }
}