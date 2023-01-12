package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.ParticipantControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin @RestController
public class ParticipantController {

    private final String _endpointDirectory = "/api/participant/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ParticipantDTO>> create(@RequestBody ParticipantDTO participant,
                                                           @PathVariable(name = "password") String password) {
        return ParticipantControllerService.get_instance().create(new Participant(participant), password);
    }
}
