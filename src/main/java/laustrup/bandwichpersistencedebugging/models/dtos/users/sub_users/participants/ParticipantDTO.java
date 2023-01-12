package laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.participants;

import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.services.DTOService;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@NoArgsConstructor @Data
public class ParticipantDTO extends UserDTO {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private UserDTO[] idols;

    public ParticipantDTO(Participant participant) {
        super(participant.get_primaryId(), participant.get_username(), participant.get_firstName(), participant.get_lastName(),
                new ContactInfoDTO(participant.get_contactInfo()), participant.get_albums(), participant.get_ratings(),
                participant.get_events(), participant.get_chatRooms(), new SubscriptionDTO(participant.get_subscription()),
                participant.get_bulletins(), participant.get_authority(), participant.get_timestamp());
        idols = new UserDTO[participant.get_idols().size()];
        for (int i = 0; i < idols.length; i++)
            idols[i] = DTOService.get_instance().convertToDTO(participant.get_idols().get(i+1));
    }

    public ParticipantDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_firstName(), user.get_lastName(),
                new ContactInfoDTO(user.get_contactInfo()), user.get_albums(), user.get_ratings(),
                user.get_events(), user.get_chatRooms(), new SubscriptionDTO(user.get_subscription()),
                user.get_bulletins(), user.get_authority(), user.get_timestamp());
        if (user.get_authority() == User.Authority.PARTICIPANT) {
            idols = new UserDTO[((Participant) user).get_idols().size()];
            for (int i = 0; i < idols.length; i++)
                idols[i] = DTOService.get_instance().convertToDTO(((Participant) user).get_idols().get(i+1));
        }
    }
}
