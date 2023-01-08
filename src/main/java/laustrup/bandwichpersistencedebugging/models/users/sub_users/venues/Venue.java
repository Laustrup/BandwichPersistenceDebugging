package laustrup.bandwichpersistencedebugging.models.users.sub_users.venues;

import laustrup.bandwichpersistencedebugging.models.chats.Request;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@NoArgsConstructor
public class Venue extends User {

    /**
     * The location that the Venue is located at, which could be an address or simple a place.
     */
    @Getter @Setter
    private String _location;

    /**
     * The description of the gear that the Venue posses.
     */
    @Getter @Setter
    private String _gearDescription;

    /**
     * All the Events that this Venue has planned.
     */
    @Getter
    private Liszt<Event> _events;

    /**
     * The size of the stage and room, that Events can be held at.
     */
    @Getter @Setter
    private int _size;

    /**
     * The Requests requested for this Venue.
     */
    @Getter
    private Liszt<Request> _requests;

    public Venue(long id) {
        super(id);
    }
    public Venue(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                 Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, String location,
                 String gearDescription, Subscription.Status subscriptionStatus, SubscriptionOffer subscriptionOffer,
                 Liszt<Bulletin> bulletins, int size, Liszt<Request> requests, LocalDateTime timestamp) {
        super(id, username, null, null, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, null),
                bulletins, Authority.VENUE, timestamp);

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _events = events;
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
        _requests = requests;

        _assembling = true;
    }

    public Venue(String username, String description, String location, String gearDescription, int size) {
        super(username, null, null, description,
                new Subscription(new Venue(), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, null, null), Authority.VENUE);

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _events = new Liszt<>();
        _requests = new Liszt<>();
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    /**
     * Sets the Users of Requests.
     * Will only be done, if it is under assembling.
     * @return The Requests of this Artist.
     */
    public Liszt<Request> set_requestUsers() {
        if (_assembling)
            for (int i = 1; i <= _requests.size(); i++)
                _requests.get(i).set_user(this);
        return _requests;
    }

    /**
     * Adds an Event to the Liszt of Events.
     * @param event An object of Event, that is wished to be added.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> add(Event event) { return add(new Event[]{event}); }

    /**
     * Adds Events to the Liszt of Events.
     * @param events An array of events, that is wished to be added.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> add(Event[] events) {
        _events.add(events);
        return _events;
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request request) { return add(new Request[]{request}); }

    /**
     * Adds Requests to the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request[] requests) {
        _requests.add(requests);
        return _requests;
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request request) { return remove(new Request[]{request}); }

    /**
     * Removes Requests of the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request[] requests) {
        _requests.remove(requests);
        return _requests;
    }

    /**
     * Removes an Event of the Liszt of Events.
     * @param event An object of Event, that is wished to be removed.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> removeEvent(Event event) { return removeEvents(new Event[]{event}); }

    /**
     * Removes Events of the Liszt of Events.
     * @param events An array of events, that is wished to be removed.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> removeEvents(Event[] events) {
        _events.remove(events);
        return _events;
    }

    @Override
    public String toString() {
        return "Venue(id="+_primaryId+
                ",username="+_username+
                ",location="+_location+
                ",description="+_description+
                ",gearDescription="+_gearDescription+
                ",timestamp="+_timestamp+
                ")";
    }
}
