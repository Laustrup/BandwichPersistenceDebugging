package laustrup.bandwichpersistencedebugging.models;

import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
@NoArgsConstructor @ToString
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    @Getter
    private Liszt<User> _users;

    /**
     * All the Events that contains similarities with a search query.
     */
    @Getter
    private Liszt<Event> _events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        _users = users;
        _events = events;
    }
}
