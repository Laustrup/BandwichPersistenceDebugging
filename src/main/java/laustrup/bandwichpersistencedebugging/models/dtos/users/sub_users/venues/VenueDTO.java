package laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.venues;

import laustrup.bandwichpersistencedebugging.models.dtos.chats.RequestDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.venues.Venue;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@NoArgsConstructor @Data
public class VenueDTO extends UserDTO {

    /**
     * The location that the Venue is located at, which could be an address or simple a place.
     */
    private String _location;

    /**
     * The description of the gear that the Venue posses.
     */
    private String _gearDescription;

    /**
     * The size of the stage and room, that Events can be held at.
     */
    private int _size;

    /**
     * The Requests requested for this Venue.
     */
    private RequestDTO[] _requests;

    public VenueDTO(Venue venue) {
        super(venue.get_primaryId(), venue.get_username(), venue.get_description(),
                new ContactInfoDTO(venue.get_contactInfo()), venue.get_albums(), venue.get_ratings(),
                venue.get_events(), venue.get_chatRooms(), new SubscriptionDTO(venue.get_subscription()),
                venue.get_bulletins(), Authority.VENUE, venue.get_timestamp());

        _location = venue.get_location();

        _gearDescription = venue.get_gearDescription();
        _size = venue.get_size();
        _requests = new RequestDTO[venue.get_requests().size()];
        for (int i = 0; i < _requests.length; i++)
            _requests[i] = new RequestDTO(venue.get_requests().get(i+1));
    }

    public VenueDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_description(),
                new ContactInfoDTO(user.get_contactInfo()), user.get_albums(), user.get_ratings(),
                user.get_events(), user.get_chatRooms(), new SubscriptionDTO(user.get_subscription()),
                user.get_bulletins(), Authority.VENUE, user.get_timestamp());

        if (user.get_authority() == User.Authority.VENUE) {
            _location = ((Venue) user).get_location();

            _gearDescription = ((Venue) user).get_gearDescription();
            _size = ((Venue) user).get_size();
            _requests = new RequestDTO[((Venue) user).get_requests().size()];
            for (int i = 0; i < _requests.length; i++)
                _requests[i] = new RequestDTO(((Venue) user).get_requests().get(i+1));
        }
    }
}
