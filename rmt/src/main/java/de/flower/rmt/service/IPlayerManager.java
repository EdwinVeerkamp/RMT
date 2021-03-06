package de.flower.rmt.service;

import de.flower.rmt.model.db.entity.Player;
import de.flower.rmt.model.db.entity.Team;
import de.flower.rmt.model.db.entity.User;
import de.flower.rmt.model.db.entity.event.Event;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * @author flowerrrr
 */
public interface IPlayerManager {

    List<Player> findAllByTeam(Team team);

    List<Player> findAllByUser(User user, Attribute... attributes);

    Player findByTeamAndUser(Team team, User user);

    Player findByEventAndUser(Event event, User user);

    void save(Player entity);

    void addPlayer(Team team, User user);

    void addPlayers(Team team, List<User> users);

    void removePlayer(Team team, Player player);

    void removeUserFromAllTeams(User user);

    /**
     * Soft deletes all players of the team.
     * Soft in case deletion of team was human error to be able to recover.
     *
     * @param entity
     */
    void deleteByTeam(Team entity);

    List<Player> sortByTeam(List<Player> list);
}
