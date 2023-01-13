package laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Card;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistencedebugging.repositories.DbGate;
import laustrup.bandwichpersistencedebugging.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistencedebugging.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistencedebugging.utilities.Plato;
import laustrup.bandwichpersistencedebugging.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Participants.
 */
public class UserPersistenceService {

    /**
     * Singleton instance of the Service.
     */
    private static UserPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserPersistenceService get_instance() {
        if (_instance == null) _instance = new UserPersistenceService();
        return _instance;
    }

    private UserPersistenceService() {}

    /**
     * Will upsert a Mail.
     * Closes database connection and sets Mail into assembled.
     * @param mail The Mail that will be upserted.
     * @return The ChatRoom of the Mail from the database.
     */
    public ChatRoom upsert(Mail mail) {
        ResultSet set = ModelRepository.get_instance().upsert(mail);
        if (set != null) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                return Assembly.get_instance().finish(
                        Assembly.get_instance().getChatRoomUnassembled(
                                set.getLong("mails.chat_room_id"))
                );
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't get ChatRoom of upserted Mail...",e);
            }
        }
        return null;
    }

    /**
     * Will upsert a ChatRoom and inserts its chatters if they exist.
     * Closes database connection and sets ChatRoom into assembled.
     * @param chatRoom The ChatRoom that will be upserted.
     * @return The ChatRoom from the database.
     */
    public ChatRoom upsert(ChatRoom chatRoom) {
        Long id = ModelRepository.get_instance().upsert(chatRoom);
        if (id!=null)
            return Assembly.get_instance().finish(
                    Assembly.get_instance().getChatRoomUnassembled(id)
            );
        return null;
    }

    /**
     * Will delete a User and connections from repository will be closed.
     * @param user The User that will be deleted.
     * @return A Plato with true truth if success, otherwise false with a message.
     */
    public Plato delete(User user) {
        if (user.get_primaryId()>0)
            return new Plato(UserRepository.get_instance().delete(user));
        Plato status = new Plato(false);
        status.set_message("Couldn't delete " + user.get_title() + "...");
        return status;
    }

    /**
     * Will upsert a Bulletin of a User.
     * Closes database connection and sets User into assembled.
     * @param bulletin The Bulletin that will be upserted.
     * @return The Receiver from the database.
     */
    public User upsert(Bulletin bulletin) {
        if (ModelRepository.get_instance().upsert(bulletin, true))
            return Assembly.get_instance().getUser(bulletin.get_receiver().get_primaryId());
        ModelRepository.get_instance().closeConnection();
        return (User) bulletin.get_receiver();
    }

    /**
     * Will upsert a Rating of a User.
     * Closes database connection and sets User into assembled.
     * @param rating The Rating that will be upserted.
     * @return The Appointed from the database.
     */
    public User upsert(Rating rating) {
        if (ModelRepository.get_instance().upsert(rating))
            return Assembly.get_instance().getUser(rating.get_appointed().get_primaryId());
        ModelRepository.get_instance().closeConnection();
        return rating.get_appointed();
    }

    /**
     * Will upsert an Album of a User.
     * Closes database connection and sets User into assembled.
     * @param album The Album that will be upserted.
     * @return The author from the database.
     */
    public User upsert(Album album) {
        ModelRepository.get_instance().upsert(album);
        return Assembly.get_instance().getUser(album.get_author().get_primaryId());
    }

    /**
     * Adds a following between two Users.
     * @param fan The User that should follow an idol.
     * @param idol The User that should being followed by a fan.
     * @return The two updated Users if success.
     */
    public User[] follow(User fan, User idol) {
        User[] users = new User[2];

        if (UserRepository.get_instance().insert(fan,idol))
            users = new User[]{
                    Assembly.get_instance().getUserUnassembled(fan.get_primaryId()),
                    Assembly.get_instance().getUserUnassembled(idol.get_primaryId())
            };

        return users;
    }

    /**
     * Removes a following between two Users.
     * @param fan The User that is following an idol.
     * @param idol The User that is being followed by a fan.
     * @return The two updated Users if success.
     */
    public User[] unfollow(User fan, User idol) {
        User[] users = new User[2];

        if (UserRepository.get_instance().remove(fan, idol))
            users = new User[]{
                    Assembly.get_instance().getUserUnassembled(fan.get_primaryId()),
                    Assembly.get_instance().getUserUnassembled(idol.get_primaryId())
            };

        DbGate.get_instance().close();
        return users;
    }

    /**
     * Will update a User, but only if the login fits the User.
     * @param user The User with values that will be updated and an id.
     * @param login Is needed to insure the User has access to this update.
     * @param password A password that will be changed.
     * @return The updated User of database values.
     */
    public User update(User user, Login login, String password) {
        if (login.passwordIsValid())
            if (Assembly.get_instance().getUserUnassembled(login).get_primaryId() == user.get_primaryId())
                if (UserRepository.get_instance().update(user, login, password))
                    return Assembly.get_instance().getUser(user.get_primaryId());

        return Assembly.get_instance().getUser(user.get_primaryId());
    }

    /**
     * Will upsert Card if it isn't null. If it is null it will upsert Subscription of User.
     * @param user The User with the Subscription to update in database.
     * @param login This Login is used to insure the action is permitted.
     * @param card The Card that will be upserted in database, if it isn't null.
     * @return The User of this upsert of values in database.
     */
    public User upsert(User user, Login login, Card card) {
        if (login.passwordIsValid()) {
            if (Assembly.get_instance().getUserUnassembled(login).get_primaryId() == user.get_primaryId()) {
                if (card != null) {
                    ResultSet set = UserRepository.get_instance().upsert(card);
                    if (set!=null) {
                        try {
                            UserRepository.get_instance().upsert(new Subscription(
                                    user, user.get_subscription().get_type(), user.get_subscription().get_status(),
                                    user.get_subscription().get_offer(), set.getLong("id"),
                                    user.get_subscription().get_timestamp()
                            ));
                        } catch (SQLException e) {
                            Printer.get_instance().print("Couldn't get generated key of Card...",e);
                        }
                    }
                } else {
                    UserRepository.get_instance().upsert(user.get_subscription());
                }
            }
        }

        return Assembly.get_instance().getUser(user.get_primaryId());
    }
}
