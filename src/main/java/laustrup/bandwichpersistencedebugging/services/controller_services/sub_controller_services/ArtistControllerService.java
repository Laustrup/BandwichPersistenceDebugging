package laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.services.controller_services.ControllerService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services.ArtistPersistenceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ArtistControllerService extends ControllerService<ArtistDTO> {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistControllerService get_instance() {
        if (_instance == null) _instance = new ArtistControllerService();
        return _instance;
    }

    private ArtistControllerService() {}

    /**
     * Will create an Artist and afterwards put it in a ResponseEntity.
     * @param artist The Artist that is wished to be created.
     * @param password The password assigned for the Artist.
     * @return A ResponseEntity with the Response of Artist and the HttpStatus.
     */
    public ResponseEntity<Response<ArtistDTO>> create(Artist artist, String password) {
        if (new Login(artist.get_username(), password).passwordIsValid())
            return entityContent(new ArtistDTO(ArtistPersistenceService.get_instance().create(artist, password)));
        else
            return new ResponseEntity<>(new Response<>(new ArtistDTO(artist),
                    Response.StatusType.INVALID_PASSWORD_FORMAT), HttpStatus.NOT_ACCEPTABLE);
    }
}
