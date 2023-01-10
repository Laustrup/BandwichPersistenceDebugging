package laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.sub_assemblings.user_assemblings;

import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembler;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Is only used for database read functions.
 * Will build Users from database row values.
 * Is a singleton.
 */
public class UserAssembly extends Assembler {

    /**
     * Singleton instance of the Service.
     */
    private static UserAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserAssembly get_instance() {
        if (_instance == null) _instance = new UserAssembly();
        return _instance;
    }

    private UserAssembly() {}

    /**
     * Builds a User object with the informations given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param login An object containing username and password.
     * @return The assembled User.
     */
    public User assemble(Login login) {
        return assemble(UserRepository.get_instance().get(login),true,false);
    }

    /**
     * Builds a User object with the informations given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param id The id of the User that is wished to be assembled.
     * @return The assembled User.
     */
    public User assemble(long id, boolean isTemplate) {
        return assemble(UserRepository.get_instance().get(id),true,isTemplate);
    }

    /**
     * Builds all the User objects, where the informations are given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @return All the assembled Users.
     */
    public Liszt<User> assembles() {
        return assembles(UserRepository.get_instance().get(), false);
    }

    /**
     * Builds User objects that are alike the search query, where the informations are given from the UserRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param searchQuery The search that should have something in common with some Users.
     * @return The assembled Users similar to the search query.
     */
    public Liszt<User> assembles(String searchQuery) {
        return assembles(UserRepository.get_instance().search(searchQuery),false);
    }

    /**
     * The private method that assembles multiple Users, that are defined in other public methods.
     * @param set The ResultSet that will define the values for the Users.
     * @return The assembled Users.
     */
    private Liszt<User> assembles(ResultSet set, boolean isTemplate) {
        Liszt<User> users = new Liszt<>();

        try {
            while (!set.isAfterLast()) {
                if (set.isBeforeFirst())
                    set.next();
                users.add(assemble(set, false, isTemplate));
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't assemble Users...", e);
            return null;
        }

        return users;
    }

    /**
     * Assembles the User from a ResultSet.
     * Will not close the Connection to the database, when it is done assembling.
     * @param set A JDBC ResultSet that is gathered from the rows of the SQL statement to the database.
     * @param preInitiate If true, it will set the ResultsSet at first row, should only be done,
     *                   if there is only expected a single entity.
     * @return The assembled User.
     */
    public User assemble(ResultSet set, boolean preInitiate, boolean isTemplate) {
        User user = null;

        try {
            if (preInitiate)
                set.next();
            switch (set.getString("users.kind")) {
                case "BAND" -> user = BandAssembly.get_instance().assemble(set, isTemplate);
                case "ARTIST" -> user = ArtistAssembly.get_instance().assemble(set,isTemplate);
                case "VENUE" -> user = VenueAssembly.get_instance().assemble(set);
                case "PARTICIPANT" -> user = ParticipantAssembly.get_instance().assemble(set);
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Trouble assembling user...", e);
        }

        return user;
    }

    public ChatRoom assembleChatRoom(ResultSet set) throws SQLException {
        long id = set.getLong("chat_rooms.id");
        String title = set.getString("chat_rooms.title");
        Liszt<Mail> mails = new Liszt<>();
        Liszt<User> chatters = new Liszt<>();
        User responsible = assemble(set.getLong("chat_rooms.responsible_id"),false);
        LocalDateTime timestamp = set.getTimestamp("chat_rooms.`timestamp`").toLocalDateTime();

        do {
            if (id != set.getLong("chat_rooms.id"))
                break;

            mails = _handler.handleMails(set, mails);
            chatters = _handler.handleChatters(set, chatters);

        } while (set.next());

        return new ChatRoom(id, title, mails, chatters, responsible, timestamp);
    }
}
