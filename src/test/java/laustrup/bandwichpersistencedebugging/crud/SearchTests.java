package laustrup.bandwichpersistencedebugging.crud;

import laustrup.bandwichpersistencedebugging.JTest;
import laustrup.bandwichpersistencedebugging.models.Search;
import laustrup.bandwichpersistencedebugging.services.RandomCreatorService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTests extends JTest {

    @Test
    void canSearch() {
        //ARRANGE
        String query = RandomCreatorService.get_instance().generateSubString(
                Assembly.get_instance().getUser(1).get_username()
        );
        if (query == null)
            query = RandomCreatorService.get_instance().generateSubString(
                    Assembly.get_instance().getEvent(1).get_title()
            );
        if (query == null)
            fail();

        //ACT
        begin();
        Search search = Assembly.get_instance().search(query);
        calculatePerformance("search " + query);

        //ARRANGE
        assertTrue(search != null && (search.getEvents().length > 0 || search.getUsers().length > 0));
    }
}
