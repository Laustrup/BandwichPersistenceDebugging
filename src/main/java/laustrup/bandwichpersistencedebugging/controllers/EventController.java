package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.events.Participation;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.EventControllerService;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin @RestController
public class EventController {

    private final String _endpointDirectory = "/api/event/";

    @PostMapping(_endpointDirectory + "get/{id}")
    public ResponseEntity<Response<Event>> get(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().get(id);
    }

    @PostMapping(_endpointDirectory + "get")
    public ResponseEntity<Response<Liszt<Event>>> get() {
        return EventControllerService.get_instance().get();
    }

    @PostMapping(value = _endpointDirectory + "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> create(@RequestBody Event event) {
        return EventControllerService.get_instance().create(event);
    }

    @DeleteMapping(_endpointDirectory + "{id}")
    public ResponseEntity<Response<Plato>> delete(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().delete(new Event(id));
    }

    @DeleteMapping(value = _endpointDirectory + "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato>> delete(@RequestBody Event event) {
        return EventControllerService.get_instance().delete(event);
    }

    @PatchMapping(value = _endpointDirectory + "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> update(@RequestBody Event event) {
        return EventControllerService.get_instance().update(event);
    }

    @PutMapping(value = _endpointDirectory + "upsert/participations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> upsert(@RequestBody Participation[] participations) {
        return EventControllerService.get_instance().upsert(new Liszt<>(participations));
    }

    @PatchMapping(value = "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> upsert(@RequestBody Bulletin bulletin) {
        return EventControllerService.get_instance().upsert(new Bulletin(
                        bulletin.get_primaryId(),bulletin.get_author(),bulletin.get_receiver(),
                        bulletin.get_content(),bulletin.is_sent(),bulletin.get_edited(),
                        bulletin.is_public(), bulletin.get_timestamp()
                )
        );
    }
}
