package de.flower.rmt.test;

import de.flower.common.util.geo.LatLng;
import de.flower.rmt.model.*;
import de.flower.rmt.model.event.*;
import de.flower.rmt.repository.*;
import de.flower.rmt.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Provides test data.
 *
 * Use #newXXX methods in unit tests.
 * Use #getXXX methods when running integration tests with underlying database.
 *
 * @author flowerrrr
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TestData {

    private final static Logger log = LoggerFactory.getLogger(TestData.class);

    @Autowired
    private ITeamRepo teamRepo;

    @Autowired
    private IClubRepo clubRepo;

    @Autowired
    private IEventRepo eventRepo;

    @Autowired
    private IPlayerRepo playerRepo;

    @Autowired
    private IActivityRepo activityRepo;

    @Autowired
    private ITeamManager teamManager;

    @Autowired
    private IUniformManager uniformManager;

    @Autowired
    private IPlayerManager playerManager;

    @Autowired
    private IEventManager eventManager;

    @Autowired
    protected IInvitationManager invitationManager;

    @Autowired
    private IVenueManager venueManager;

    @Autowired
    private IOpponentManager opponentManager;

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IActivityManager activityManager;

    private Random random = new Random();

    /**
     * Determines type of created test event.
     */
    private EventType eventType = EventType.Training;

    public TestData() {
        // interesting, constructor called twice.
        // see http://forum.springsource.org/showthread.php?9814-Constructor-called-twice-when-using-CGLIB-proxy-beans
        // log.info("TestData.<init>");
    }

    public void setEventType(final EventType eventType) {
        this.eventType = eventType;
    }

    public void checkDataConsistency(EntityManager em) {
/*
        // check that invitation->event->team matches invittee->user->team
        Query query = em.createQuery("from Invitation r  where r.event.team != r.player.team");
        List list = query.getResultList();
        Assert.assertTrue(list.isEmpty(), list.toString());
*/
    }

    public Club getClub() {
        // load some often used entities
        return Validate.notNull(clubRepo.findOne(1L));
    }

    public static Club newClub() {
        Club club = new Club("Foo FC");
        return club;
    }

    public Team getJuveAmateure() {
        return Validate.notNull(teamRepo.findOne(1L));
    }

    public Team createTeam(String name) {
        Team team = teamManager.newInstance();
        team.setName(name);
        teamManager.save(team);
        return team;
    }

    public Team createTeamWithPlayers(String name, int numPlayers) {
        Team team = createTeam(name);
        List<User> users = createUsers(numPlayers - 1);
        playerManager.addPlayers(team, users);
        // add user of security context to team
        playerManager.addPlayer(team, getTestUser());
        return team;
    }

    public Team newTeamWithPlayers(int numPlayers) {
        Team team = newTeam();
        List<User> users = newUsers(numPlayers);
        for (User user : users) {
            addPlayer(team, user);
        }
        return team;
    }

    public Team newTeam() {
        Team team = new Team(newClub());
        team.setName("Amateure");
        return team;
    }

    public User newUser() {
        User user = new User(newClub());
        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@acme.com");
        user.setFullname(RandomStringUtils.randomAlphabetic(10));
        user.setInitialPassword("1234");
        user.setEncryptedPassword("io8ujalöjdfkalsöj");
        user.setStatus(random.nextBoolean() ? User.Status.FIT : User.Status.INJURED);
        Role role = new Role(Role.Roles.PLAYER.getRoleName());
        user.getRoles().add(role);
        role.setUser(user);
        // some get manager role
        if (random.nextBoolean()) {
            role = new Role(Role.Roles.MANAGER.getRoleName());
            user.getRoles().add(role);
            role.setUser(user);
        }
        return user;
    }

    public User newUserWithTeams() {
        Club club = newClub();
        Team team1 = new Team(club);
        Team team2 = new Team(club);
        Team team3 = new Team(club);
        User user = newUser();
        // add user to teams
        addPlayer(team1, user);
        addPlayer(team2, user);
        addPlayer(team3, user);
        return user;
    }

    private Player addPlayer(final Team team, final User user) {
        Player player = new Player(team, user);
        player.setRetired(false);
        player.setOptional(false);
        player.setNotification(true);
        team.getPlayers().add(player);
        user.getPlayers().add(player);
        return player;
    }

    public User createUser() {
        return createUsers(1).get(0);
    }

    public List<User> createUsers(int count) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < count; i++) {
            User user = userManager.newInstance();
            user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@mailinator.com");
            user.setFullname(RandomStringUtils.randomAlphabetic(10));
            userManager.save(user);
            users.add(user);
        }
        return users;
    }

    public List<User> newUsers(int count) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < count; i++) {
            User user = newUser();
            users.add(user);
        }
        return users;
    }

    public Event newEvent() {
        Team team = newTeamWithPlayers(15);
        Event event = eventType.newInstance(team.getClub());
        event.setTeam(team);
        event.setDate(new Date());
        event.setTime(new LocalTime());
        if (EventType.isSoccerEvent(event)) {
            ((AbstractSoccerEvent) event).setKickoff(new LocalTime());
            ((AbstractSoccerEvent) event).setUniform(newUniformList(event.getTeam()).get(0));
        }
        event.setVenue(newVenue(event.getTeam().getClub()));
        event.setSummary("2. Training");
        event.setComment("Kommt ja nicht zu spät!");
        for (Player player : event.getTeam().getPlayers()) {
            // create invitations
            Invitation invitation = new Invitation(event, player.getUser());
            event.getInvitations().add(invitation);
        }
        return event;
    }

    public Event createEvent(Team team, boolean createInvitations) {
        Event event = eventManager.newInstance(eventType);
        event.setDate(new Date());
        event.setTime(LocalTime.now());
        event.setSummary("Summary");
        event.setTeam(team);
        event.setVenue(venueManager.loadById(1L));
        if (EventType.isSoccerEvent(event)) {
            ((AbstractSoccerEvent) event).setKickoff(LocalTime.now());
            ((AbstractSoccerEvent) event).setUniform(createUniformList(team).get(0));
        }
        if (EventType.isMatch(event)) {
            ((Match) event).setOpponent(opponentManager.loadById(1L));
        }
        eventManager.create(event, createInvitations);
        return eventManager.loadById(event.getId(), Event_.team/*, Event_.invitations - does not work because of subclassing issues */);
    }

    public Event createEvent() {
        Team team = createTeamWithPlayers("FCB " + System.currentTimeMillis(), 15);
        Event event = createEvent(team, true);
        return event;
    }

    /**
     * Creates an event with some responses.
     */
    public Event createEventWithResponses() {
        Event event = createEvent();
        List<Invitation> invitations = invitationManager.findAllByEvent(event);
        respond(invitations.get(0), RSVPStatus.ACCEPTED, "some comment");
        respond(invitations.get(2), RSVPStatus.DECLINED, "some comment");
        respond(invitations.get(3), RSVPStatus.ACCEPTED, "some comment");
        respond(invitations.get(5), RSVPStatus.UNSURE, "some comment");
        respond(invitations.get(7), RSVPStatus.ACCEPTED, "some comment");
        respond(invitations.get(8), RSVPStatus.DECLINED, "some comment");
        respond(invitations.get(9), RSVPStatus.DECLINED, "some comment");
        respond(invitations.get(11), RSVPStatus.ACCEPTED, "some comment");
        respond(invitations.get(12), RSVPStatus.ACCEPTED, "some comment");
        // init collections
        event.getTeam().getPlayers().size();
        event.getTeam().getPlayers().get(0).getUser();
        return event;
    }

    private void respond(Invitation invitation, RSVPStatus status, String comment) {
        invitation.setStatus(status);
        invitation.setComment(comment);
        invitationManager.save(invitation);
    }

    public User getTestUser() {
        return userManager.findByUsername(AbstractIntegrationTests.testUserName);
    }

    public Venue newVenue(Club club) {
        Venue venue = new Venue(club);
        venue.setName("Böglwiese");
        venue.setAddress("Putzbrunner Str. 13\n81739 München");
        venue.setLatLng(new LatLng(48.12123, 11.01231231));
        return venue;
    }

    public List<Uniform> createUniformList(Team team) {
        List<Uniform> uniforms = newUniformList(team);
        for (Uniform uniform : uniforms) {
            uniformManager.save(uniform);
        }
        return uniformManager.findAllByTeam(team);
    }

    public List<Uniform> newUniformList(Team team) {
        Uniform j1 = new Uniform(team);
        j1.setName("Trikotsatz 1");
        j1.setShirt("white/red stripes");
        j1.setShorts("red");
        j1.setSocks("white");
        Uniform j2 = new Uniform(team);
        j2.setName("Trikotsatz 2");
        j2.setShirt("black");
        j2.setShorts("red");
        j2.setSocks("gold");
        return Arrays.asList(j1, j2);
    }

    public List<Activity> newActivities(final int num) {
        Club club = newClub();
        List<Activity> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Activity entity = new Activity(club);
            entity.setDate(new DateTime().minusMillis(RandomUtils.nextInt((int) TimeUnit.DAYS.toMillis(10))).toDate());
            entity.setMessage("Some message " + RandomStringUtils.randomAscii(20));
            list.add(entity);
        }
        // sort desc by date
        Collections.sort(list, new Comparator<Activity>() {
            @Override
            public int compare(final Activity o1, final Activity o2) {
                return (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });
        return list;
    }

    public void createActivities(final int num) {
        Club club = getClub();
         for (int i = 0; i < num; i++) {
             Activity entity = new Activity(club);
             entity.setDate(new DateTime().minusMillis(RandomUtils.nextInt((int) TimeUnit.DAYS.toMillis(10))).toDate());
             entity.setMessage("Some message " + RandomStringUtils.randomAscii(20));
             activityRepo.save(entity);
         }
    }
}