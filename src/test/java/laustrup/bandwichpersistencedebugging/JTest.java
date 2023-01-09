package laustrup.bandwichpersistencedebugging;

import laustrup.bandwichpersistencedebugging.models.chats.messages.Bulletin;
import laustrup.bandwichpersistencedebugging.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistencedebugging.utilities.Liszt;
import laustrup.bandwichpersistencedebugging.utilities.Printer;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Adds a few functions to test methods to reuse.
 */
public abstract class JTest extends Asserter {

    /**
     * Will be the start of the ACT in tests
     * Needs to be set before the ACT.
     * Is use for checking performance
     */
    protected LocalDateTime _start;

    /**
     * Contains different generated items to use for testing.
     * Are being reset for each method.
     */
    protected TestItems _items;

    /**
     * A default password, with the purpose of creating, logging in and various alike features.
     */
    protected final String _password = "123456789%&";

    /**
     * This Random is the java Random utility, that can be reused throughout tests.
     */
    protected Random _random = new Random();

    /**
     * Will automatically begin the time of start.
     * Uses the @BeforeEach notation.
     * If there is an ARRANGE, please use begin() before ACT,
     * in order not to get an untrustworthy performance time result.
     */
    @BeforeEach
    public void setup() {
        _items = new TestItems();
        begin();
    }

    /**
     * Sets the start time of ACT for measuring of performance time.
     * Must only use before act.
     * Is also used in @BeforeEach, so in case of no ARRANGE, this method is not needed.
     */
    protected void begin() { _start = LocalDateTime.now(); }

    /**
     * Calculates the performance time from start to this moment and prints it in milliseconds.
     * @return The duration of the performance in milliseconds
     */
    protected long calculatePerformance() {
        long performance = Duration.between(_start, LocalDateTime.now()).toMillis();

        Printer.get_instance().print("The performance of current test is " + performance +
                " in milliseconds and " + (performance / 1000) + " in minutes." );

        return performance;
    }

    /**
     * Calculates the performance time from start to this moment and prints it in milliseconds.
     * @param title The title of the ACT
     * @return The duration of the performance in milliseconds
     */
    protected long calculatePerformance(String title) {
        long performance = Duration.between(_start, LocalDateTime.now()).toMillis();

        Printer.get_instance().print("The performance of current " + title + " is " + performance +
                " in milliseconds and " + (performance / 1000) + " in minutes." );

        return performance;
    }
}
