package laustrup.bandwichpersistencedebugging.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.Response;
import laustrup.bandwichpersistencedebugging.models.Search;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Card;
import laustrup.bandwichpersistencedebugging.services.controller_services.ControllerService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services.UserPersistenceService;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import laustrup.bandwichpersistencedebugging.utilities.Plato;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerService extends ControllerService<User> {

    /**
     * Singleton instance of the Service.
     */
    private static UserControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserControllerService get_instance() {
        if (_instance == null) _instance = new UserControllerService();
        return _instance;
    }

    private UserControllerService() {}

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for logging in.
     * Uses an assemblyService for reading the database and building the User object.
     * Checks if the username is its email and that the email of the object isn't null.
     * @param login An object containing username and password.
     * @return The created ResponseEntity of a User as Response.
     */
    public ResponseEntity<Response<User>> get(Login login) {
        User user = Assembly.get_instance().getUser(login);
        if (user == null)
            return new ResponseEntity<>(new Response<>(null, Response.StatusType.WRONG_PASSWORD),
                    HttpStatus.NOT_ACCEPTABLE);

        if ((login.usernameIsEmailKind() &&
                (user.get_contactInfo().get_email() != null || !user.get_contactInfo().get_email().isEmpty())) ||
                !login.usernameIsEmailKind())
            return entityContent(user);

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a User by its id.
     * Uses an assemblyService for reading the database and building the User object.
     * @param id The id of the User, that is wished to be gathered.
     * @return The created ResponseEntity of a User as Response.
     */
    public ResponseEntity<Response<User>> get(long id) {
        return entityContent(Assembly.get_instance().getUser(id));
    }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting all Users.
     * Uses an assemblyService for reading the database and building the User objects.
     * @return The created ResponseEntity of all Users as Response.
     */
    public ResponseEntity<Response<Liszt<User>>> get() { return entityContent(Assembly.get_instance().getUsers()); }

    /**
     * Creates a ResponseEntity for a controller to send to client.
     * This scenario is for getting a Search by a String query.
     * Uses an assemblyService for reading the database and building the Search object.
     * @param query The String query of the Search, that is wished to be gathered.
     * @return The created ResponseEntity of a Response of Search.
     */
    public ResponseEntity<Response<Search>> search(String query) { return searchContent(Assembly.get_instance().search(query)); }

    /**
     * Will delete User and create a ResponseEntity with a Response that includes its status of the delete.
     * @param user The User that should be deleted.
     * @return The created ResponseEntity of a Response with the status of the delete.
     */
    public ResponseEntity<Response<Plato>> delete(User user) {
        return platoContent(UserPersistenceService.get_instance().delete(user));
    }

    /**
     * Will upsert Bulletin of a User and create a ResponseEntity with a Response of the current state of Receiver.
     * @param bulletin The Bulletin that should be upserted.
     * @return The created ResponseEntity of a Response with the current state of Receiver.
     */
    public ResponseEntity<Response<User>> upsert(Bulletin bulletin) {
        return entityContent(UserPersistenceService.get_instance().upsert(bulletin));
    }

    /**
     * Will upsert Rating of a User and create a ResponseEntity with a Response of the current state of Appointed.
     * @param rating The Rating that should be upserted.
     * @return The created ResponseEntity of a Response with the current state of Appointed.
     */
    public ResponseEntity<Response<User>> upsert(Rating rating) {
        return entityContent(UserPersistenceService.get_instance().upsert(rating));
    }

    /**
     * Will upsert an Album of a User with its items
     * and create a ResponseEntity with a Response of the current state of Author.
     * @param album The Album that should be upserted.
     * @return The created ResponseEntity of the author of the Album.
     */
    public ResponseEntity<Response<User>> upsert(Album album) {
        return entityContent(UserPersistenceService.get_instance().upsert(album));
    }

    /**
     * Makes a following between two Users.
     * @param fan The User that should follow an idol.
     * @param idol The User that should being followed by a fan.
     * @return A Response of the updated Users.
     */
    public ResponseEntity<Response<User[]>> follow(User fan, User idol) {
        return entityContent(UserPersistenceService.get_instance().follow(fan, idol));
    }

    /**
     * Removes a following between two Users.
     * @param fan The User that shouldn't follow an idol.
     * @param idol The User that shouldn't being followed by a fan.
     * @return A Response of the updated Users.
     */
    public ResponseEntity<Response<User[]>> unfollow(User fan, User idol) {
        return entityContent(UserPersistenceService.get_instance().unfollow(fan, idol));
    }

    /**
     * Will update a User, but only if the login fits the User.
     * @param user The User with values that will be updated and an id.
     * @param login Is needed to insure the User has access to this update.
     * @param password A password that will be changed.
     * @return A Response of the updated User.
     */
    public ResponseEntity<Response<User>> update(User user, Login login, String password) {
        return entityContent(UserPersistenceService.get_instance().update(user, login, password));
    }

    /**
     * Will upsert Card for a User.
     * @param userId The if of the User with values that will be updated and an id.
     * @param login Is needed to insure the User has access to this update.
     * @param card The Card that will be upserted in database.
     * @return The User of the upserted Card.
     */
    public ResponseEntity<Response<User>> upsert(long userId, Login login, Card card) {
        return entityContent(UserPersistenceService.get_instance().upsert(
                Assembly.get_instance().getUserUnassembled(userId), login, card)
        );
    }

    /**
     * Will upsert Subscription for a User.
     * @param user The User with values that will be updated and an id.
     * @param login Is needed to insure the User has access to this update.
     * @return The User of the upserted Subscription.
     */
    public ResponseEntity<Response<User>> upsert(User user, Login login) {
        return entityContent(UserPersistenceService.get_instance().upsert(user,login,null));
    }
}