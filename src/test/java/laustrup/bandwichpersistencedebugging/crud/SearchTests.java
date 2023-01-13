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

        //ACT
        begin();
        Search search = Assembly.get_instance().search(query);
        calculatePerformance("search " + query);

        //ARRANGE
        assertTrue(search != null && (search.getUsers().length > 0));
    }
}
