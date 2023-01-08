package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.ArtistControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtistController {

    private final String _endpointDirectory = "/api/artist/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Artist>> create(@RequestBody Artist artist,
                                                   @PathVariable(name = "password") String password) {
        return ArtistControllerService.get_instance().create(new Artist(
                artist.get_username(),artist.get_firstName(),artist.get_lastName(), artist.get_description(),
                artist.get_subscription(), artist.get_contactInfo(),artist.get_bands(), artist.get_runner()), password
        );
    }
}