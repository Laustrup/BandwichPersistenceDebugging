package laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistencedebugging.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistencedebugging.repositories.sub_repositories.ArtistRepository;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.EntityService;
import laustrup.bandwichpersistencedebugging.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Artists.
 */
public class ArtistPersistenceService extends EntityService<Artist> {

    /**
     * Singleton instance of the Service.
     */
    private static ArtistPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ArtistPersistenceService get_instance() {
        if (_instance == null) _instance = new ArtistPersistenceService();
        return _instance;
    }

    private ArtistPersistenceService() {}

    /**
     * Will create an Artist by using ArtistRepository.
     * Only does this, if id doesn't already exist.
     * Will also include the generated key.
     * Uses Assembly to get the values from the database,
     * to insure it exists and also to close connections.
     * @param artist The Artist that will be created.
     * @param password The password for the Artist.
     * @return If success, the created Artist with its generated key, otherwise null.
     */
    public Artist create(Artist artist, String password) {
        if (artist.get_primaryId() == 0) {
            ResultSet set = ArtistRepository.get_instance().create(artist, password);
            Subscription subscription = artist.get_subscription();
            ContactInfo contactInfo = artist.get_contactInfo();

            try {
                if (set.isBeforeFirst())
                    set.next();
                artist = (Artist) Assembly.get_instance().getUserUnassembled(set.getLong("users.id"));
            } catch (SQLException e) {
                Printer.get_instance().print("ResultSet error in Artist create service...", e);
                return null;
            }

            //Puts in subscription and contactInfo
            artist = new Artist(artist.get_primaryId(), artist.get_username(), artist.get_firstName(), artist.get_lastName(),
                    artist.get_description(),contactInfo,artist.get_albums(), artist.get_ratings(), artist.get_events(),
                    artist.get_gigs(),artist.get_chatRooms(),subscription,artist.get_bulletins(), artist.get_bands(),
                    artist.get_runner(), artist.get_fans(),artist.get_idols(),artist.get_requests(),artist.get_timestamp()
            );

            if (upsert(artist))
                return artist;
        }
        return null;
    }
}