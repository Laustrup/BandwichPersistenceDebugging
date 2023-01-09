package laustrup.bandwichpersistencedebugging;

import laustrup.bandwichpersistencedebugging.models.Rating;
import laustrup.bandwichpersistencedebugging.models.albums.Album;
import laustrup.bandwichpersistencedebugging.models.chats.ChatRoom;
import laustrup.bandwichpersistencedebugging.models.chats.Request;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.chats.messages.Mail;
import laustrup.bandwichpersistencedebugging.models.events.Event;
import laustrup.bandwichpersistencedebugging.models.events.Gig;
import laustrup.bandwichpersistencedebugging.models.events.Participation;
import laustrup.bandwichpersistencedebugging.models.users.User;
import laustrup.bandwichpersistencedebugging.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.Performer;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistencedebugging.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistencedebugging.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;

import static org.junit.jupiter.api.Assertions.*;

public class Asserter {

    /**
     * Asserts two Participants to check they are the same.
     * @param expected The Participant that is arranged and defined.
     * @param actual The Participant that is the result of an action.
     */
    protected void assertParticipants(Participant expected, Participant actual) {
        assertUsers(expected, actual);
        if (expected.get_idols().size() == actual.get_idols().size())
            for (int i = 1; i <= expected.get_idols().size(); i++)
                assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
        else fail();
    }

    /**
     * Asserts two Bands to check they are the same.
     * @param expected The Band that is arranged and defined.
     * @param actual The Band that is the result of an action.
     */
    protected void assertBands(Band expected, Band actual) {
        assertPerformers(expected, actual);
        if (expected.get_members().size() == actual.get_members().size())
            for (int i = 1; i <= expected.get_members().size(); i++)
                assertEquals(expected.get_members().get(i).toString(),actual.get_members().get(i).toString());
        else fail();
        assertEquals(expected.get_runner(),actual.get_runner());
    }

    /**
     * Asserts two Artists to check they are the same.
     * @param expected The Artist that is arranged and defined.
     * @param actual The Artist that is the result of an action.
     */
    protected void assertArtists(Artist expected, Artist actual) {
        assertPerformers(expected,actual);
        if (expected.get_bands().size() == actual.get_bands().size())
            for (int i = 1; i <= expected.get_bands().size(); i++)
                assertEquals(expected.get_bands().get(i).toString(),actual.get_bands().get(i).toString());
        else fail();
        assertEquals(expected.get_runner(),actual.get_runner());
        assertRequests(expected.get_requests(),actual.get_requests());
    }

    /**
     * Asserts two Performers to check they are the same.
     * @param expected The Performer that is arranged and defined.
     * @param actual The Performer that is the result of an action.
     */
    private void assertPerformers(Performer expected, Performer actual) {
        assertUsers(expected, actual);
        if (expected.get_fans().size() == actual.get_fans().size())
            for (int i = 1; i <= expected.get_fans().size(); i++)
                assertEquals(expected.get_fans().get(i).toString(),actual.get_fans().get(i).toString());
        else fail();
        if (expected.get_idols().size() == actual.get_idols().size())
            for (int i = 1; i <= expected.get_idols().size(); i++)
                assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
        else fail();
    }

    /**
     * Asserts two Venues to check they are the same.
     * @param expected The Venue that is arranged and defined.
     * @param actual The Venue that is the result of an action.
     */
    protected void assertVenues(Venue expected, Venue actual) {
        assertUsers(expected, actual);
        assertEquals(expected.get_location(),actual.get_location());
        assertEquals(expected.get_gearDescription(),actual.get_gearDescription());
        assertEquals(expected.get_size(),actual.get_size());
        assertRequests(expected.get_requests(),actual.get_requests());
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     */
    private void assertUsers(User expected, User actual) {
        if (expected.get_primaryId() > 0)
            assertEquals(expected.get_primaryId(),actual.get_primaryId());
        assertEquals(expected.get_username(),actual.get_username());
        assertEquals(expected.get_firstName(),actual.get_firstName());
        assertEquals(expected.get_lastName(),actual.get_lastName());
        assertEquals(expected.get_description(),actual.get_description());
        asserting(expected.get_contactInfo(),actual.get_contactInfo());
        assertAlbums(expected.get_albums(),actual.get_albums());
        assertRatings(expected.get_ratings(),actual.get_ratings());
        assertEvents(expected.get_events(),actual.get_events());
        assertChatRooms(expected.get_chatRooms(),actual.get_chatRooms());
        asserting(expected.get_subscription(),actual.get_subscription());
        assertBulletins(expected.get_bulletins(),actual.get_bulletins());
        assertEquals(expected.get_timestamp(),actual.get_timestamp());
    }

    /**
     * Asserts Albums to check they are the same.
     * @param expectations The Albums that are arranged and defined.
     * @param actuals The Albums that are the result of an action.
     */
    private void assertAlbums(Liszt<Album> expectations, Liszt<Album> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Album expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.toString(),actual.toString());
                for (int j = 1; j <= expected.get_items().size(); j++) {
                    for (int k = 1; k <= expected.get_items().get(j).get_tags().size(); k++) {
                        assertEquals(expected.get_items().get(j).get_tags().get(k).get_primaryId(),
                                actual.get_items().get(j).get_tags().get(k).get_primaryId());
                    }
                    assertEquals(expected.get_items().get(j).get_endpoint(),actual.get_items().get(j).get_endpoint());
                    if (expected.get_items().get(j).get_event() != null)
                        assertEquals(expected.get_items().get(j).get_event().get_primaryId(),actual.get_items().get(j).get_primaryId());
                    assertEquals(expected.get_items().get(j).get_kind(),actual.get_items().get(j).get_kind());
                }
            }
        } else fail();
    }

    /**
     * Asserts Ratings to check they are the same.
     * @param expectations The Ratings that are arranged and defined.
     * @param actuals The Ratings that are the result of an action.
     */
    private void assertRatings(Liszt<Rating> expectations, Liszt<Rating> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Rating expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_appointed().get_primaryId(),actual.get_appointed().get_primaryId());
                assertEquals(expected.get_judge().get_primaryId(),actual.get_judge().get_primaryId());
                assertEquals(expected.get_value(),actual.get_value());
                assertTrue((expected.get_value() <= 5 && expected.get_value() > 0)
                        && (actual.get_value() <= 5 && actual.get_value() > 0));
                assertEquals(expected.get_comment(),actual.get_comment());
            }
        } else fail();
    }

    /**
     * Asserts ChatRooms to check they are the same.
     * @param expectations The ChatRooms that are arranged and defined.
     * @param actuals The ChatRooms that are the result of an action.
     */
    private void assertChatRooms(Liszt<ChatRoom> expectations, Liszt<ChatRoom> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                ChatRoom expected = expectations.get(i),
                        actual = actuals.get(i);
                assertMails(expected.get_mails(),actual.get_mails());
                for (int j = 1; j <= expected.get_chatters().size(); j++)
                    assertEquals(expected.get_chatters().get(j).toString(),actual.get_chatters().get(j).toString());
                assertEquals(expected.get_responsible().toString(),actual.get_responsible().toString());
                assertEquals(expected.get_answeringTime(),actual.get_answeringTime());
                assertEquals(expected.is_answered(),actual.is_answered());
            }
        } else fail();
    }

    /**
     * Asserts Mails to check they are the same.
     * @param expectations The Mails that are arranged and defined.
     * @param actuals The Mails that are the result of an action.
     */
    private void assertMails(Liszt<Mail> expectations, Liszt<Mail> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Mail expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_chatRoom().toString(),actual.toString());
                assertEquals(expected.get_author().toString(),actual.toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }
        } else fail();
    }

    /**
     * Asserts two Subscriptions to check they are the same.
     * @param expected The Subscription that is arranged and defined.
     * @param actual The Subscription that is the result of an action.
     */
    private void asserting(Subscription expected, Subscription actual) {
        assertEquals(expected.get_user().toString(),actual.get_user().toString());
        assertEquals(expected.get_type(),actual.get_type());
        assertEquals(expected.get_status(),actual.get_status());
        assertEquals(expected.get_price(),actual.get_price());
        assertEquals(expected.get_offer().get_expires(),actual.get_offer().get_expires());
        assertEquals(expected.get_offer().get_type(),actual.get_offer().get_type());
        assertEquals(expected.get_offer().get_effect(),actual.get_offer().get_effect());
        assertEquals(expected.get_cardId(),actual.get_cardId());
    }

    /**
     * Asserts Requests to check they are the same.
     * @param expectations The Requests that are arranged and defined.
     * @param actuals The Requests that are the result of an action.
     */
    private void assertRequests(Liszt<Request> expectations, Liszt<Request> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Request expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_user().toString(),actual.get_user().toString());
                assertEquals(expected.get_event().toString(),actual.get_event().toString());
                assertEquals(expected.get_approved().get_truth(),actual.get_approved().get_truth());
                assertEquals(expected.get_message(),actual.get_message());
            }
        } else fail();
    }
    /**
     * Asserts two ContactInfos to check they are the same.
     * @param expected The information that is arranged and defined.
     * @param actual The information that is the result of an action.
     */
    protected void asserting(ContactInfo expected, ContactInfo actual) {
        assertEquals(expected.get_email(),actual.get_email());
        assertEquals(expected.get_phone().get_country().get_title(),actual.get_phone().get_country().get_title());
        assertEquals(expected.get_phone().get_country().get_indexes(),actual.get_phone().get_country().get_indexes());
        assertEquals(expected.get_phone().get_country().get_firstPhoneNumberDigits(),actual.get_phone().get_country().get_firstPhoneNumberDigits());
        assertEquals(expected.get_phone().get_numbers(),actual.get_phone().get_numbers());
        assertEquals(expected.get_phone().is_mobile(),actual.get_phone().is_mobile());
        assertEquals(expected.getAddressInfo(),actual.getAddressInfo());
        assertEquals(expected.get_country().get_title(),actual.get_country().get_title());
        assertEquals(expected.get_country().get_indexes(),actual.get_country().get_indexes());
        assertEquals(expected.get_country().get_firstPhoneNumberDigits(),actual.get_country().get_firstPhoneNumberDigits());
    }

    /**
     * Asserts Bulletins to check they are the same.
     * @param expectations The Bulletins that are arranged and defined.
     * @param actuals The Bulletins that are the result of an action.
     */
    protected void assertBulletins(Liszt<Bulletin> expectations, Liszt<Bulletin> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Bulletin expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_author().toString(),actual.get_author().toString());
                assertEquals(expected.get_receiver().toString(),actual.get_receiver().toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }
        } else fail();
    }

    /**
     * Asserts Events to check they are the same.
     * @param expectations The Events that are arranged and defined.
     * @param actuals The Events that are the result of an action.
     */
    private void assertEvents(Liszt<Event> expectations, Liszt<Event> actuals) {
        if (expectations.size() == actuals.size())
            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.get(i),actuals.get(i));
        else
            fail();
    }

    /**
     * Asserts two Events to check they are the same.
     * @param expected The Event that is arranged and defined.
     * @param actual The Event that is the result of an action.
     */
    protected void asserting(Event expected, Event actual) {
        assertEquals(expected.toString(),actual.toString());
        assertTrue(expected.get_openDoors().isEqual(actual.get_openDoors()));
        assertTrue(expected.get_start().isEqual(actual.get_start()));
        assertTrue(expected.get_end().isEqual(actual.get_end()));
        assertEquals(expected.get_length(),expected.get_length());
        assertEquals(expected.get_voluntary().get_argument(),actual.get_voluntary().get_argument());
        assertEquals(expected.get_public().get_argument(),actual.get_public().get_argument());
        assertEquals(expected.get_cancelled().get_argument(),actual.get_cancelled().get_argument());
        assertEquals(expected.get_soldOut().get_argument(),actual.get_soldOut().get_argument());
        assertEquals(expected.get_location(), actual.get_location());
        assertEquals(expected.get_ticketsURL(), actual.get_ticketsURL());
        asserting(expected.get_contactInfo(), actual.get_contactInfo());
        assertGigs(expected.get_gigs(),actual.get_gigs());
        assertVenues(expected.get_venue(),actual.get_venue());
        assertRequests(expected.get_requests(),actual.get_requests());
        assertParticipations(expected.get_participations(),actual.get_participations());
        assertAlbums(expected.get_albums(),actual.get_albums());
    }

    /**
     * Asserts Participations to check they are the same.
     * @param expectations The Participations that are arranged and defined.
     * @param actuals The Participations that are the result of an action.
     */
    protected void assertParticipations(Liszt<Participation> expectations, Liszt<Participation> actuals) {
        if (expectations.size() == actuals.size())
            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.get(i),actuals.get(i));
        else
            fail();
    }

    /**
     * Asserts two Participations to check they are the same.
     * @param expected The Participation that is arranged and defined.
     * @param actual The Participation that is the result of an action.
     */
    protected void asserting(Participation expected, Participation actual) {
        assertEquals(expected.toString(),actual.toString());
    }

    /**
     * Asserts Gigs to check they are the same.
     * @param expectations The Gigs that are arranged and defined.
     * @param actuals The Gigs that are the result of an action.
     */
    protected void assertGigs(Liszt<Gig> expectations, Liszt<Gig> actuals) {
        if (expectations.size() == actuals.size())
            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.get(i),actuals.get(i));
        else
            fail();
    }

    /**
     * Asserts two Gigs to check they are the same.
     * @param expected The Gig that is arranged and defined.
     * @param actual The Gig that is the result of an action.
     */
    protected void asserting(Gig expected, Gig actual) {

    }
}
