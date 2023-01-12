package laustrup.bandwichpersistencedebugging.services;

import laustrup.bandwichpersistencedebugging.models.Model;
import laustrup.bandwichpersistencedebugging.models.dtos.ModelDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.events.EventDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.BandDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.venues.VenueDTO;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.users.User;

public class DTOService {

    /**
     * Singleton instance of the Service.
     */
    private static DTOService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static DTOService get_instance() {
        if (_instance == null) _instance = new DTOService();
        return _instance;
    }

    private DTOService() {}

    public UserDTO convertToDTO(User user) {
        switch (user.get_authority()) {
            case VENUE -> { return new VenueDTO(user); }
            case ARTIST -> { return new ArtistDTO(user); }
            case BAND -> { return new BandDTO(user); }
            case PARTICIPANT -> { return new ParticipantDTO(user); }
            default -> { return null; }
        }
    }

    public ModelDTO convertToDTO(Model model) {
        switch (model.getClass().getName()) {
            case "VENUE" -> { return new VenueDTO((User) model); }
            case "ARTIST" -> { return new ArtistDTO((User) model); }
            case "BAND" -> { return new BandDTO((User) model); }
            case "PARTICIPANT" -> { return new ParticipantDTO(((User) model)); }
            default -> { return new EventDTO((Event) model); }
        }
    }
}
