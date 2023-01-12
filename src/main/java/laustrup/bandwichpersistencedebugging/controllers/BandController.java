package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.BandControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin @RestController
public class BandController {

    private final String _endpointDirectory = "/api/band/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Band>> create(@RequestBody Band band,
                                                 @PathVariable(name = "password") String password) {
        return BandControllerService.get_instance().create(new Band(
                band.get_username(), band.get_description(), band.get_subscription(),
                        band.get_contactInfo(), band.get_members()),
                password
        );
    }
}