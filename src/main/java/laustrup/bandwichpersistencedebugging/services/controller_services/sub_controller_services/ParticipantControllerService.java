package laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.services.controller_services.ControllerService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services.ParticipantPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ParticipantControllerService extends ControllerService<Participant> {

    /**
     * Singleton instance of the Service.
     */
    private static ParticipantControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ParticipantControllerService get_instance() {
        if (_instance == null) _instance = new ParticipantControllerService();
        return _instance;
    }

    private ParticipantControllerService() {}

    /**
     * Will create an Participant and afterwards put it in a ResponseEntity.
     * @param participant The Participant that is wished to be created.
     * @return A ResponseEntity with the Response of Participant and the HttpStatus.
     */
    public ResponseEntity<Response<Participant>> create(Participant participant, String password) {
        if (new Login(participant.get_username(), password).passwordIsValid())
            return entityContent(ParticipantPersistenceService.get_instance().create(participant,password));
        else
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.INVALID_PASSWORD_FORMAT),
                    HttpStatus.NOT_ACCEPTABLE);
    }
}
