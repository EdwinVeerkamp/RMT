package de.flower.rmt.service;

import com.google.common.annotations.VisibleForTesting;
import com.mysema.query.types.Path;
import de.flower.common.ui.ajax.dragndrop.DraggableDto;
import de.flower.rmt.model.db.entity.Invitation;
import de.flower.rmt.model.db.entity.Lineup;
import de.flower.rmt.model.db.entity.LineupItem;
import de.flower.rmt.model.db.entity.event.Event;

import java.util.List;

/**
 * @author flowerrrr
 */
public interface ILineupManager {

    Lineup findLineup(Event entity);

    Lineup findOrCreateLineup(Event event, Path<?>... attributes);

    List<LineupItem> findLineupItems(Event event, Path<?>... attributes);

    List<Invitation> findInvitationsInLinuep(Event event);

    Lineup createLineup(final Event event);

    void drop(DraggableDto dto);

    void removeLineupItem(Long invitationId);

    void delete(Long lineupId);

    void publishLineup(Event event);

    @VisibleForTesting
    @Deprecated
    void save(LineupItem item);
}
