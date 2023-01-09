package laustrup.bandwichpersistencedebugging.crud;

import laustrup.bandwichpersistencedebugging.JTest;
import laustrup.bandwichpersistencedebugging.models.Search;
import laustrup.bandwichpersistencedebugging.services.RandomCreatorService;
import laustrup.bandwichpersistencedebugging.services.persistence_services.assembling_services.Assembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTests extends JTest {

    @Test
    void canSearch() {
        //ARRANGE
        String query = RandomCreatorService.get_instance().generateString();
        Search search;
        int tries = 0;

        //ACT
        do {
            begin();
            search = Assembly.get_instance().search(query);
            calculatePerformance("search " + query + " try " + tries);
            if (tries > 100)
                break;
            query = RandomCreatorService.get_instance().generateString();
            tries++;
        } while (search == null);

        //ARRANGE
        assertTrue(search != null && (!search.get_events().isEmpty() || !search.get_users().isEmpty()));
    }
}
