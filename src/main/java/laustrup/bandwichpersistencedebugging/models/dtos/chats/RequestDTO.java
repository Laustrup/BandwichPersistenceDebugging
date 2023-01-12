package laustrup.bandwichpersistencedebugging.models.dtos.chats;

import laustrup.bandwichpersistencedebugging.models.chats.Request;
import laustrup.bandwichpersistencedebugging.models.dtos.ModelDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.events.EventDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.services.DTOService;
import laustrup.bandwichpersistencedebugging.utilities.Plato;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@NoArgsConstructor @Data
public class RequestDTO extends ModelDTO {

    /**
     * The User that needs to approve the Event.
     */
    private UserDTO _user;

    /**
     * The Event that has been requested for.
     */
    private EventDTO _event;

    /**
     * The value that indicates if the request for the Event has been approved.
     */
    private Plato.Argument _approved;

    /**
     * This message will be shown for the user, in order to inform of the request.
     */
    private String _message;

    public RequestDTO(Request request) {
        super(request.get_user().get_primaryId(), request.get_event().get_primaryId(),
                "Request of " + request.get_user().get_username() + " to " + request.get_event().get_title(),
                request.get_timestamp());
        _user = DTOService.get_instance().convertToDTO(request.get_user());
        _event = new EventDTO(request.get_event());
        _approved = request.get_approved().get_argument();
        _message = request.get_message();
    }
}
