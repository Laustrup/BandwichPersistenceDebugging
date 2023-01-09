package laustrup.bandwichpersistencedebugging.crud;

import laustrup.bandwichpersistencedebugging.JTest;
import laustrup.bandwichpersistencedebugging.models.users.Login;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistencedebugging.services.persistence_services.entity_services.sub_entity_services.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserCRUDTests extends JTest {

    @Test
    void canCRUDArtist() {
        //ARRANGE
        Artist expected = _items.get_artists()[_random.nextInt(_items.get_artistAmount())];
        User.Authority authority = User.Authority.ARTIST;

        //ACT
        begin();
        Artist actual = ArtistPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDVenue() {
        //ARRANGE
        Venue expected = _items.get_venues()[_random.nextInt(_items.get_venueAmount())];
        User.Authority authority = User.Authority.VENUE;

        //ACT
        begin();
        Venue actual = VenuePersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDParticipant() {
        //ARRANGE
        Participant expected = _items.get_participants()[_random.nextInt(_items.get_participantAmount())];
        User.Authority authority = User.Authority.PARTICIPANT;

        //ACT
        begin();
        Participant actual = ParticipantPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDBand() {
        //ARRANGE
        Band expected = _items.get_bands()[_random.nextInt(_items.get_bandAmount())];
        User.Authority authority = User.Authority.BAND;

        //ACT
        begin();
        Band actual = BandPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    private void readUpdateDelete(User expected, User actual, User.Authority authority) {
        if (actual != null) {
            expected = actual;

            //ACT
            actual = read(expected, authority);

            //ASSERT
            asserting(expected, actual, authority);

            if (actual != null) {
                // ACT
                actual = logIn(expected, authority);

                // ASSERT
                asserting(expected, actual, authority);

                if (actual != null) {
                    // ACT
                    expected.set_description("This is a new description");
                    actual = update(expected,_password,authority);

                    // ASSERT
                    asserting(expected, actual, authority);
                } else fail();
            } else fail();
        } else fail();

        // ACT and ASSERT
        assertTrue(delete(actual == null ? expected : actual, authority));
    }
    private User logIn(User user, User.Authority authority) {
        begin();
        user = Assembly.get_instance().getUser(new Login(user.get_username(),_password));
        calculatePerformance("login " + authority);
        return user;
    }
    private User read(User user, User.Authority authority) {
        begin();
        user = Assembly.get_instance().getUser(user.get_primaryId());
        calculatePerformance("read " + authority);
        return user;
    }
    private User update(User user, String password, User.Authority authority) {
        begin();
        user = UserPersistenceService.get_instance().update(user, new Login(user.get_username(), password), "%&123456789");
        calculatePerformance("read " + authority);
        return user;
    }
    private boolean delete(User user, User.Authority authority) {
        begin();
        boolean result = UserPersistenceService.get_instance().delete(user).get_truth();
        calculatePerformance("delete " + authority);
        return result;
    }
    private void asserting(User expected, User actual, User.Authority authority) {
        switch (authority) {
            case PARTICIPANT -> assertParticipants((Participant) expected, (Participant) actual);
            case BAND -> assertBands((Band) expected,(Band) actual);
            case ARTIST -> assertArtists((Artist) expected,(Artist) actual);
            case VENUE -> assertVenues((Venue) expected,(Venue) actual);
            default -> fail();
        }
    }
}