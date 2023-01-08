package laustrup.bandwichpersistencedebugging.controllers;

import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.Search;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Card;
import laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services.UserControllerService;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final String _endpointDirectory = "/api/user/";

    @PostMapping(value = _endpointDirectory + "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> logIn(@RequestBody Login login) {
        return UserControllerService.get_instance().get(login);
    }

    @PostMapping(_endpointDirectory + "get/{id}")
    public ResponseEntity<Response<User>> get(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().get(id);
    }

    @PostMapping(_endpointDirectory + "get")
    public ResponseEntity<Response<Liszt<User>>> get() {
        return UserControllerService.get_instance().get();
    }

    @PostMapping(_endpointDirectory + "search/{search_query}")
    public ResponseEntity<Response<Search>> search(@PathVariable(name = "search_query") String searchQuery) {
        return UserControllerService.get_instance().search(searchQuery);
    }

    @DeleteMapping(_endpointDirectory + "{id}")
    public ResponseEntity<Response<Plato>> delete(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().delete(new Participant(id));
    }

    @DeleteMapping(value = _endpointDirectory + "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato>> delete(@RequestBody User user) {
        return UserControllerService.get_instance().delete(user);
    }

    @PatchMapping(value = _endpointDirectory + "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Bulletin bulletin) {
        return UserControllerService.get_instance().upsert(new Bulletin(
                    bulletin.get_primaryId(),bulletin.get_author(),bulletin.get_receiver(),
                    bulletin.get_content(),bulletin.is_sent(),bulletin.get_edited(),
                    bulletin.is_public(), bulletin.get_timestamp()
                )
        );
    }

    @PatchMapping(value = _endpointDirectory + "upsert/rating", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Rating rating) {
        return UserControllerService.get_instance().upsert(new Rating(
                    rating.get_value(), rating.get_appointed(),
                    rating.get_judge(), rating.get_timestamp()
                )
        );
    }

    @PatchMapping(value = _endpointDirectory + "upsert/album", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Album album) {
        return UserControllerService.get_instance().upsert(new Album(
                    album.get_title(), album.get_items(), album.get_author()
                )
        );
    }

    @PutMapping(value = _endpointDirectory + "follow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User[]>> follow(@RequestBody User[] users) {
        return UserControllerService.get_instance().follow(users[0], users[1]);
    }

    @DeleteMapping(value = _endpointDirectory + "unfollow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User[]>> unfollow(@RequestBody User[] users) {
        return UserControllerService.get_instance().unfollow(users[0], users[1]);
    }

    @PatchMapping(value = _endpointDirectory + "update/{login_username}/{login_password}/{password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> update(@RequestBody User user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String loginPassword,
                                                 @PathVariable(name = "password") String password) {
        return UserControllerService.get_instance().update(user,new Login(username,loginPassword),password);
    }

    @PatchMapping(value = _endpointDirectory + "upsert/card/{user_id}/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Card card,
                                                 @PathVariable(name = "user_id") long id,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(id, new Login(username,password), card);
    }

    @PatchMapping(value = _endpointDirectory + "upsert/subscription/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody User user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(user, new Login(username, password));
    }
}
