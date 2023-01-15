package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.ArtistControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class ArtistController {

    private final String _endpointDirectory = "/api/artist/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ArtistDTO>> create(@RequestBody ArtistDTO artist,
                                                      @PathVariable(name = "password") String password) {
        return ArtistControllerService.get_instance().create(new Artist(artist), password);
    }
}