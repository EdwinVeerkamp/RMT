package de.flower.rmt.service;

import de.flower.rmt.model.Team;
import de.flower.rmt.model.User;
import de.flower.rmt.model.User_;
import de.flower.rmt.test.AbstractIntegrationTests;
import org.hibernate.LazyInitializationException;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * @author flowerrrr
 */

public class UserManagerTest extends AbstractIntegrationTests {

    /**
     * Test if clients of service layer can specify
     * eager fetching of domain objects to avoid lazy init execptions.
     */
    @Test
    public void testEagerFetching() {
        List<User> users = userManager.findAll(User_.roles);
        assertTrue(users.size() > 0);
        User user = users.get(0);
        log.info(user.toString());
        log.info(user.getRoles().get(0).getAuthority());
    }

    /**
     * Test that forces lazy init ex.
     */
    @Test(expectedExceptions = {LazyInitializationException.class})
    public void testLazyInitException() {
        List<User> users = userManager.findAll();
        assertTrue(users.size() > 0);
        User user = users.get(0);
        log.info(user.getRoles().get(0).getAuthority());
    }

    @Test
    public void testSaveUser() {
        User user = userManager.newInstance();
        user.setEmail("foo@bar.com");
        user.setFullname("Foo Bar");
        userManager.save(user);

        // test reattach
        user = userManager.loadById(user.getId());
        String fullname = "John Doe";
        user.setFullname(fullname);
        userManager.save(user);
        user = userManager.loadById(user.getId());
        assertEquals(user.getFullname(), fullname);
    }

    @Test
    public void testUnassignedPlayer() {
        Team team = testData.getJuveAmateure();
        List<User> users = userManager.findUnassignedPlayers(team);
        assertFalse(users.isEmpty());
        for (User u : users) {
            teamManager.addPlayer(team, u);
        }
        users = userManager.findUnassignedPlayers(team);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testResetPassword() {
        User user = testData.createUser();
        user.setInitialPassword(null);
        userRepo.save(user);
        userManager.resetPassword(user, true);
        // now reload user and assert that new password has been generated
        user = userManager.loadById(user.getId());
        assertNotNull(user.getInitialPassword());
    }

 }