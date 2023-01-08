package laustrup.bandwichpersistencedebugging.repositories.sub_repositories;

import laustrup.bandwichpersistencedebugging.models.chats.Request;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.events.Gig;
import laustrup.bandwichpersistencedebugging.models.events.Participation;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.Performer;
import laustrup.bandwichpersistencedebugging.repositories.Repository;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Are handling Repository actions for Events.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class EventRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static EventRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventRepository get_instance() {
        if (_instance == null) _instance = new EventRepository();
        return _instance;
    }

    private EventRepository() {}

    /**
     * Will collect a JDBC ResultSet of Participations from the database, by using a SQL statement.
     * @param ids The ids of the Events of the Participations
     * @return The collected JDBC ResultSet.
     */
    public ResultSet participations(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("event_id.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM participations " + where + ";");
    }

    /**
     * Will collect a JDBC ResultSet of all Events from the database, by using a SQL statement.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get() {
        return get("");
    }

    /**
     * Will collect a JDBC ResultSet of an Event from the database, by using a SQL statement.
     * @param id The id of the Event, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(long id) {
        return get("WHERE `events`.id = " + id);
    }

    /**
     * Will collect a JDBC ResultSet of several Events from the database, by using a SQL statement.
     * @param ids The ids of the Events, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("`events`.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return get(where.toString());
    }

    /**
     * Will collect a JDBC ResultSet of all Events that has something in common with a search query from the database,
     * by using a SQL statement.
     * It will compare columns of titles, descriptions and locations.
     * Doesn't order them by relevance at the moment.
     * @param query The search query, that is a line that should have something in common with columns.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet search(String query) {
        query = query.replaceAll("%","");
        return get("WHERE `events`.title LIKE '%" + query + "%' OR " +
                "`events`.description LIKE '%" + query + "%' OR " +
                "`events`.location LIKE '%" + query + "%'");
    }

    /**
     * This is the method that can through a SQL statement find and collect the Event.
     * Both from an id/ids or a search query, that is given from other public methods.
     * @param where The where statement, that decides what information that is being looked for.
     * @return The collected JDBC ResultSet.
     */
    private ResultSet get(String where) {
        return read("SELECT * FROM `events` " +
                "INNER JOIN contact_informations ON `events`.venue_id = contact_informations.id " +
                "INNER JOIN gigs ON `events`.id = gigs.event_id " +
                "INNER JOIN acts ON gigs.id = acts.gig_id " +
                "INNER JOIN requests ON `events`.id = requests.event_id " +
                "INNER JOIN participations ON `events`.id = participations.event_id " +
                "INNER JOIN event_bulletins ON `events`.id = event_bulletins.receiver_id " +
                "INNER JOIN album_relations ON `events`.id = album_relations.event_id " +
                "INNER JOIN albums ON album_relations.album_id = albums.id " +
                "INNER JOIN album_endpoints ON album.id = album_endpoints " +
                "INNER JOIN users ON `events`.venue_id = users.id OR acts.user_id = users.id " +
                    "OR requests.user_id = users.id OR participations.participant_id = users.id " +
                        "OR event_bulletins.author_id = users.id OR album_relations.user_id = users.id " +
                where + ";");
    }

    /**
     * Will create aN Event and get the generated key value if success.
     * @param event The Event that will be created.
     * @return A ResultSet of the created values with the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet create(Event event) {
        try {
            return create("INSERT INTO `events`(" +
                        "title," +
                        "open_doors," +
                        "`description`," +
                        "is_voluntary," +
                        "is_public," +
                        "is_cancelled," +
                        "is_sold_out," +
                        "location," +
                        "price," +
                        "tickets_url," +
                        "venue_id," +
                        "`timestamp`" +
                    ") " +
                    "VALUES ('" +
                        event.get_title() +"','" +
                        event.get_openDoors() + "','" +
                        event.get_description() + "','" +
                        event.get_voluntary().get_argument() + "','" +
                        event.get_public().get_argument() + "','" +
                        event.get_cancelled().get_argument() + "','" +
                        event.get_soldOut().get_argument() + "','" +
                        event.get_location() + "'," +
                        event.get_price() + ",'" +
                        event.get_ticketsURL() + "'," +
                        event.get_venue().get_primaryId() + "," +
                    "NOW());").getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Event...", e);
        }
        return null;
    }

    /**
     * Will delete Event by its id and all child tables with its foreign key cascade.
     * Closes connection.
     * @param event The Event that should be deleted.
     * @return True if connection is closed and the Event doesn't exist.
     */
    public boolean delete(Event event) {
        return delete(event.get_primaryId(), "`events`", "id", true);
    }

    /**
     * Will update Event with its Gigs and Requests.
     * Doesn't close connection.
     * @param event The Event that will be updated.
     * @return True if it is a success.
     */
    public boolean update(Event event) {
        String sql = "UPDATE `events` " +
                    "title = '" + event.get_title() + "', " +
                    "open_doors = '" + event.get_openDoors() + "', " +
                    "`description` = '" + event.get_description() + "', " +
                    "is_voluntary = '" + event.get_voluntary() + "', " +
                    "is_public = '" + event.get_public() + "', " +
                    "is_cancelled = '" + event.get_cancelled() + "', " +
                    "is_sold_out = '" + event.get_soldOut() + "', " +
                    "location = '" + event.get_location() + "', " +
                    "price = " + event.get_price() + ", " +
                    "tickets_url = '" + event.get_ticketsURL() + "', " +
                    "venue_id = " + event.get_venue().get_primaryId() + " " +
                "WHERE id = " + event.get_primaryId() + "; ";

        for (Gig gig : event.get_gigs()) {
            boolean idExists = gig.get_primaryId() > 0;
            sql += "INSERT INTO gigs(" +
                        (idExists ? "id," : "") +
                        "event_id," +
                        "`start`," +
                        "`end`," +
                        "`timestamp`" +
                    ") " +
                    "VALUES(" +
                        (idExists ? gig.get_primaryId()+"," : "") +
                        gig.get_event().get_primaryId() + ",'" +
                        gig.get_start() + "','" +
                        gig.get_end() + "'," +
                    "NOW()) " +
                    "ON DUPLICATE KEY UPDATE " +
                        "`start` = '" + gig.get_start() + "', " +
                        "`end` = '" + gig.get_end() + "'" +
                    "; ";

            //TODO Add timestamp to database table of acts
            for (Performer performer : gig.get_act())
                sql += "INSERT INTO IGNORE acts(" +
                            "user_id," +
                            "gig_id" +
                        ") " +
                        "VALUES(" +
                            performer.get_primaryId() + "," +
                            gig.get_primaryId() +
                        "); ";
        }

        return edit(sql + upsertRequestSQL(event.get_requests()), false);
    }

    /**
     * Makes an Upserts Requests SQL statement.
     * If the User and Events are already inserted, it will update is approved and message.
     * Doesn't close connection.
     * @param requests The Requests that will be upserted.
     * @return The SQL statement.
     */
    private String upsertRequestSQL(Liszt<Request> requests) {
        String sql = new String();
        for (Request request : requests)
            sql += "INSERT INTO requests(" +
                        "user_id," +
                        "event_id," +
                        "is_approved," +
                        "message," +
                        "`timestamp`" +
                    ") " +
                    "VALUES(" +
                        request.get_user().get_primaryId() + "," +
                        request.get_event().get_primaryId() + ",'" +
                        request.get_approved().get_argument() + "','" +
                        request.get_message() + "'," +
                    "NOW()); " +
                    "ON DUPLICATE KEY UPDATE " +
                        "is_approved = '" + request.get_approved().get_argument() + "' " +
                        "message = '" + request.get_message() + "' " +
                    "; ";
        return sql;
    }

    /**
     * Upserts a single Participation.
     * If the Participant and Event is already inserted, it will update type.
     * Doesn't close connection.
     * @param participation The Participation that will be upserted.
     * @return True if it is a success.
     */
    public boolean upsert(Participation participation) {
        return upsert(new Liszt<>(new Participation[]{participation}));
    }

    /**
     * Upserts Participations.
     * If the Participants and Events are already inserted, it will update type.
     * Doesn't close connection.
     * @param participations The Participations that will be upserted.
     * @return True if it is a success and the Participations aren't empty.
     */
    public boolean upsert(Liszt<Participation> participations) {
        String sql = new String();
        for (Participation participation : participations)
            sql += "INSERT INTO participations(" +
                        "event_id," +
                        "participant_id," +
                        "`type`" +
                        "`timestamp`" +
                    ") " +
                    "VALUES(" +
                        participation.get_event().get_primaryId() + "," +
                        participation.get_participant() + ",'" +
                        participation.get_type() + "'" +
                    "NOW()) " +
                    "ON DUPLICATE KEY UPDATE " +
                        "`type` = '" + participation.get_type() +
                    "'; ";
        return (!sql.isEmpty() ? edit(sql,false) : false);
    }
}
