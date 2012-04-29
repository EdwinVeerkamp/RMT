package de.flower.rmt.service.mail;

import de.flower.rmt.model.Uniform;
import de.flower.rmt.model.User;
import de.flower.rmt.model.event.Event;
import de.flower.rmt.model.event.EventType;
import de.flower.rmt.model.event.Match;
import de.flower.rmt.model.type.Notification;
import de.flower.rmt.test.AbstractWicketIntegrationTests;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Must extend wicket tests as long as resource string lookup is done with wicket libs.
 *
 * @author flowerrrr
 */
public class NotificationServiceTest extends AbstractWicketIntegrationTests {

    @Test
    public void testSendResetPasswordMail() {
        User user = testData.createUser();
        notificationService.sendResetPasswordMail(user, securityService.getUser());
    }

    @Test
    public void testSendInvitationNewUserMail() {
        User user = testData.createUser();
        notificationService.sendInvitationNewUser(user, securityService.getUser());
    }

    @Test
    public void testNewEventNotification() {
        Event event = testData.createEvent();
        // load from db to get version with uninitialized associations
        event = eventManager.loadById(event.getId());
        Uniform uniform = uniformManager.findAllByTeam(event.getTeam()).get(0); // must be same as in create event
        String eventLink = "http://flower.de/event/4";
        Notification notification = notificationService.newEventNotification(event, eventLink);
        log.info(notification.getSubject());
        log.info(notification.getBody());
        assertFalse(notification.getBody().contains("Gegner"));
        assertTrue(notification.getBody().contains("Trikotsatz: Hemd: " + uniform.getShirt()
                + ", Hose: " + uniform.getShorts() + ", Stutzen: " + uniform.getSocks()));

        // no venue selected yet
        testData.setEventType(EventType.Match);
        Match match = (Match) testData.createEvent();
        match.setVenue(null);
        match.getSurfaceList().clear();
        match.setUniform(null);
        eventManager.save(match);
        notification = notificationService.newEventNotification(match, eventLink);
        log.info(notification.getSubject());
        log.info(notification.getBody());
        assertTrue(notification.getBody().contains("Ort: <noch nicht festgelegt>"));
        assertTrue(notification.getBody().contains("Untergrund: <unbekannt>"));
        assertTrue(notification.getBody().contains("Trikotsatz: <nicht festgelegt>"));
        assertTrue(notification.getBody().contains("Gegner"));
    }
}
