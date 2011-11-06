package de.flower.rmt.service;

import de.flower.common.util.Check;
import de.flower.rmt.model.Player;
import de.flower.rmt.model.RSVPStatus;
import de.flower.rmt.model.Response;
import de.flower.rmt.model.User;
import de.flower.rmt.model.event.Event;
import de.flower.rmt.repository.IResponseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author flowerrrr
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ResponseManager extends AbstractService implements IResponseManager {

    @Autowired
    private IResponseRepo responseRepo;

    @Autowired
    private IPlayerManager playerManager;

    @Override
    public Response newInstance(final Event event) {
        Player player = playerManager.findByEventAndUser(event, securityService.getCurrentUser());
        return new Response(event, player);
    }

    @Override
    public Response findById(final Long id) {
        return responseRepo.findOne(id);
    }

    @Override
    public List<Response> findByEventAndStatus(Event event, RSVPStatus rsvpStatus) {
        return responseRepo.findByEventAndStatusOrderByDateAsc(event, rsvpStatus);
    }

    @Override
    public Response findByEventAndUser(Event event, User user) {
        // find player instance of this user belonging to the events team
        Player player = playerManager.findByTeamAndUser(event.getTeam(), user);
        Check.notNull(player, "User [%s] not associated to event [%s]", user.getId(), event.getTeam().getId());
        return responseRepo.findByEventAndPlayer(event, player);
    }

    @Override
    @Transactional(readOnly = false)
    public Response save(final Response response) {
        validate(response);
        // in case the status changes update the date of response.
        if (!response.isTransient()) {
            Response origResponse = responseRepo.findOne(response.getId());
            if (origResponse.getStatus() != response.getStatus()) {
                response.setDate(new Date());
            }
        }
        return responseRepo.save(response);
    }

    @Override
    @Transactional(readOnly = false)
    public Response respond(Event event, Player player, RSVPStatus status, String comment) {
        // TODO (flowerrrr - 23.10.11) do some checking if responding to event is allowed
        // is this a first response or an update?
        Response response = responseRepo.findByEventAndPlayer(event, player);
        if (response == null) {
            response = new Response(event, player);
        }
        response.setStatus(status);
        response.setComment(comment);
        return this.save(response);
    }

}