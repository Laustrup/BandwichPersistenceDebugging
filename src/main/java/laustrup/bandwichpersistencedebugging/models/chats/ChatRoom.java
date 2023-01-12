package laustrup.bandwichpersistencedebugging.models.chats;

import laustrup.bandwichpersistencedebugging.models.Model;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.chats.messages.MailDTO;
import laustrup.bandwichpersistencedebugging.models.dtos.users.UserDTO;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistencedebugging.services.DTOService;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
public class ChatRoom extends Model {

    /**
     * All the Mails that has been sent will be stored here.
     */
    @Getter
    private Liszt<Mail> _mails;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    @Getter
    private Liszt<User> _chatters;

    /**
     * This responsible are being calculated for answeringTime.
     */
    @Getter
    private User _responsible;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    @Getter
    private Long _answeringTime;

    /**
     * Is true if the responsible has answered with a message.
     */
    @Getter
    private boolean _answered;

    public ChatRoom(ChatRoomDTO chatRoom) {
        _mails = new Liszt<>();
        convert(chatRoom.getMails());
        _chatters = new Liszt<>();
        convert(chatRoom.getChatters());
        _responsible = DTOService.get_instance().convertFromDTO(chatRoom.getResponsible());

        isTheChatRoomAnswered();
    }
    private Liszt<Mail> convert(MailDTO[] mails) {
        for (MailDTO mail : mails)
            _mails.add(new Mail(mail));
        return _mails;
    }
    private Liszt<User> convert(UserDTO[] chatters) {
        for (UserDTO chatter : chatters)
            _chatters.add(DTOService.get_instance().convertFromDTO(chatter));
        return _chatters;
    }
    public ChatRoom(long id, String title, Liszt<Mail> mails, Liszt<User> chatters, User responsible, LocalDateTime timestamp) {
        super(id, title.isEmpty() || title == null ? "ChatRoom-"+id : title, timestamp);
        _mails = mails;
        _chatters = chatters;
        _responsible = responsible;
        _assembling = true;

        setChatRoomOfMails();
        isTheChatRoomAnswered();
    }

    public ChatRoom(long id, String title, LocalDateTime timestamp) {
        super(id, title.isEmpty() || title == null ? "ChatRoom-"+id : title, timestamp);
        _mails = new Liszt<>();
        _chatters = new Liszt<>();
        _assembling = true;
    }

    public ChatRoom(String title, Liszt<User> chatters, User responsible) {
        super(title);
        _mails = new Liszt<>();
        _chatters = chatters;
        _responsible = responsible;

        isTheChatRoomAnswered();
    }

    /**
     * Can only be used, if it is being assembled.
     * Will set each Mail's ChatRoom to this object.
     * @return All Mails.
     */
    private Liszt<Mail> setChatRoomOfMails(){
        if (_assembling) {
            for (int i = 1; i <= _mails.size(); i++)
                _mails.get(i).set_chatRoom(this);

            _assembling = false;
        }
        return _mails;
    }

    /**
     * Adds a Mail to the ChatRoom, if the author of the Mail is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param mail A Mail object, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public List<Mail> add(Mail mail) {
        if (chatterExists(mail.get_author())) {
            if (_mails.add(mail)) if (mail.doSend()) edit(mail);
            if (!_answered) isTheChatRoomAnswered();
        }

        return _mails;
    }

    /**
     * It will add a chatter, if it isn't already added.
     * If the chatter is a Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatter A user that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public List<User> add(User chatter) {
        if (chatter.getClass() == Band.class) {
            for (Artist artist : ((Band) chatter).get_members()) {
                if (!chatterExists(artist))
                    _chatters.add(chatter);
            }
        }
        else if (!chatterExists(chatter)) _chatters.add(chatter);

        return _chatters;
    }

    /**
     * Checks if a chatter exists in the ChatRoom.
     * @param chatter A User, that should be checked, if it already exists in the ChatRoom.
     * @return True if the chatter exists in the ChatRoom.
     */
    public boolean chatterExists(User chatter) {
        for (User user : _chatters) {
            if (user.getClass() == chatter.getClass() && user.get_primaryId() == chatter.get_primaryId()) return true;
        }
        return false;
    }

    /**
     * Will remove a Mail from the ChatRoom.
     * @param mail The Mail object that is wished to be removed.
     * @return All the Mails of this ChatRoom.
     */
    public List<Mail> remove(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_primaryId() == mail.get_primaryId()) {
                _mails.remove(_mails.get(i));
                break;
            }
        }
        return _mails;
    }

    /**
     * Will remove a chatter from the ChatRoom.
     * @param chatter A user object that is wished to be removed.
     * @return All the chatters of this ChatRoom.
     */
    public List<User> remove(User chatter) {
        for (int i = 1; i <= _chatters.size(); i++) {
            if (_chatters.get(i).get_primaryId() == chatter.get_primaryId()) {
                _chatters.remove(_chatters.get(i));
                break;
            }
        }
        return _chatters;
    }

    /**
     * Edits a Mail of the ChatRoom.
     * @param mail The Mail that is an updated version of a previous Mail, which will be updated.
     * @return True if it will be edited correctly.
     */
    public boolean edit(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_primaryId() == mail.get_primaryId())
                return mail == _mails.set(i, mail);
        }
        return false;
    }

    /**
     * Checks if the ChatRoom is answered by the responsible, by a foreach loop through _mails.
     * Needs to be used each time a message is added, if the ChatRoom isn't already answered by the responsible.
     * Also use in constructor of use from database.
     * In case return is true, it will also calculate answering time.
     * @return The boolean answer of whether the ChatRoom has been answered or not
     */
    private boolean isTheChatRoomAnswered() { return findResponsibleAnswer()!=null; }

    /**
     * Calculates the time it took the responsible to answer.
     * Should be used only in local method isTheChatRoomAnswered().
     * @return The amount of hours it took the responsible to answer,
     * if ChatRoom is not answered, it will return null.
     */
    private Long calculateAnsweringTime() {
        if (_answered) {
            _answeringTime = Duration.between(_mails.get(1).get_timestamp(),
                    findResponsibleAnswer().get_timestamp()).toMinutes();
            return _answeringTime;
        }
        return null;
    }

    /**
     * Searches through the Mails and checks if the responsible have answered,
     * @return If the responsible have answered, it will return that Mail, otherwise null.
     */
    private Mail findResponsibleAnswer() {
        for (Mail mail : _mails)
            if (mail.get_author().get_primaryId() == _responsible.get_primaryId())
                return mail;

        return null;
    }

    @Override
    public String toString() {
        return "ChatRoom(" +
                    "id:" + _primaryId +
                    ",title:" + _title +
                    ",timestamp:" + _timestamp +
                ")";
    }
}
