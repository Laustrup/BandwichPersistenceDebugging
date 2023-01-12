package laustrup.bandwichpersistencedebugging.services;

import laustrup.bandwichpersistencedebugging.models.Model;
import laustrup.bandwichpersistencedebugging.models.dtos.ModelDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.events.EventDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.PerformerDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.BandDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.venues.VenueDTO;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.Performer;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.venues.Venue;

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

    public User convertFromDTO(UserDTO user) {
        switch (user.getAuthority()) {
            case VENUE -> { return new Venue((VenueDTO) user); }
            case ARTIST -> { return new Artist((ArtistDTO) user); }
            case BAND -> { return new Band((BandDTO) user); }
            case PARTICIPANT -> { return new Participant((ParticipantDTO) user); }
            default -> { return null; }
        }
    }

    public Model convertFromDTO(ModelDTO model) {
        switch (model.getClass().getName()) {
            case "VENUE" -> { return new Venue((VenueDTO) model); }
            case "ARTIST" -> { return new Artist((ArtistDTO) model); }
            case "BAND" -> { return new Band((BandDTO) model); }
            case "PARTICIPANT" -> { return new Participant(((ParticipantDTO) model)); }
            default -> { return new Event((EventDTO) model); }
        }
    }

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
            case "VENUE" -> { return new VenueDTO((Venue) model); }
            case "ARTIST" -> { return new ArtistDTO((Artist) model); }
            case "BAND" -> { return new BandDTO((Band) model); }
            case "PARTICIPANT" -> { return new ParticipantDTO(((Participant) model)); }
            default -> { return new EventDTO((Event) model); }
        }
    }
}
