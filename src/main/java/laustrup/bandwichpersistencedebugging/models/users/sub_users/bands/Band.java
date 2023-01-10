package laustrup.bandwichpersistencedebugging.models.users.sub_users.bands;

import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.events.Gig;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.Performer;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.InputMismatchException;

/**
 * Extends performer and contains Artists as members
 */
@NoArgsConstructor
public class Band extends Performer {

    /**
     * Contains all the Artists, that are members of this band.
     */
    @Getter
    private Liszt<Artist> _members;

    /**
     * A description of the gear, that the band possesses and what they require for an Event.
     */
    @Getter @Setter
    private String _runner;

    public Band(long id) {
        super(id);
    }

    public Band(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms,
                Subscription subscription, Liszt<Bulletin> bulletins, Liszt<Artist> members,
                String runner, Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, Authority.BAND, albums, ratings, events, gigs, chatRooms,
                subscription, bulletins, fans, idols, timestamp);
        _username = username;

        _members = members;

        _runner = runner;
        _assembling = true;
    }

    public Band(String username, String description, Subscription subscription, ContactInfo contactInfo,
                Liszt<Artist> members) throws InputMismatchException {
        super(username, description, subscription, Authority.BAND);
        _username = username;
        _contactInfo = contactInfo;

        _members = members;
        if (_members.isEmpty())
            throw new InputMismatchException();

        _assembling = true;
    }

    /**
     * Adds an Artist to the Liszt of members.
     * @param artist An object of Artist, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMember(Artist artist) { return addMembers(new Artist[]{artist}); }

    /**
     * Adds Artists to the Liszt of members.
     * @param artists An array of artists, that is wished to be added.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> addMembers(Artist[] artists) {
        _members.add(artists);
        return _members;
    }

    /**
     * Removes an Artist of the Liszt of members.
     * @param artist An object of Artist, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> removeMember(Artist artist) { return removeMembers(new Artist[]{artist}); }

    /**
     * Removes Artists of the Liszt of members.
     * @param artists An array of artists, that is wished to be removed.
     * @return The whole Liszt of members.
     */
    public Liszt<Artist> removeMembers(Artist[] artists) {
        _members.remove(artists);
        return _members;
    }

    /**
     * Removes a Fan of the Liszt of fans.
     * @param fan An object of Fan, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> removeFan(Participant fan) { return removeFans(new User[]{fan}); }

    /**
     * Removes Fans of the Liszt of fans.
     * @param fans An array of fans, that is wished to be removed.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> removeFans(User[] fans) {
        _fans.remove(fans);
        return _fans;
    }

    @Override
    public String toString() {
        return "Band(" +
                    "id=" + _primaryId +
                    ",username=" + _username +
                    ",description=" + _description +
                    ",timestamp=" + _timestamp +
                    ",runner=" + _runner +
                ")";
    }
}
