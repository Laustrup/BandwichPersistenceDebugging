package laustrup.bandwichpersistencedebugging.models.events;

import laustrup.bandwichpersistencedebugging.models.Model;
import laustrup.bandwichpersistencedebugging.models.dtos.events.ParticipationDTO;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;

import laustrup.bandwichpersistencedebugging.services.DTOService;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Determines type of which a Participant is participating in an Event.
 */
@Data
public class Participation extends Model {

    /**
     * The Participant of the participation.
     */
    private Participant _participant;

    /**
     * The Event of the participation.
     */
    private Event _event;

    /**
     * The type of which participant is participating in the participation.
     */
    private ParticipationType _type;

    public Participation(ParticipationDTO participation) {
        super(participation.getEvent().getPrimaryId(), participation.getParticipant().getPrimaryId(),
                "Participation of participant " +
                        participation.getParticipant().getPrimaryId() + " AND Event " +
                        participation.getEvent().getPrimaryId()
        );
        _participant = (Participant) DTOService.get_instance().convertFromDTO(participation.getParticipant());
        _event = new Event(participation.getEvent());
        _type = ParticipationType.valueOf(participation.getType().toString());
    }
    public Participation(Participant participant, Event event, ParticipationType type) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId());
        _participant = participant;
        _event = event;
        _type = type;
    }

    public Participation(Participant participant, Event event, ParticipationType type, LocalDateTime timestamp) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId(), timestamp);
        _participant = participant;
        _event = event;
        _type = type;
    }

    @Override
    public String toString() {
        return "Participation(" +
                    "primaryId:" + _primaryId +
                    ",secondaryId:" + _secondaryId +
                    ",title:" + _title +
                    ",type:" + _type +
                    ",timestamp:" + _timestamp +
                ")";
    }

    /**
     * Each Participation have four different choices of participating.
     */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
}