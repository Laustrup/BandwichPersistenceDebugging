package laustrup.bandwichpersistencedebugging.services;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RandomCreatorService {

    /**
     * Random utility for making random elements.
     */
    private Random _random = new Random();

    /**
     * Singleton instance of the Service.
     */
    private static RandomCreatorService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static RandomCreatorService get_instance() {
        if (_instance == null) _instance = new RandomCreatorService();
        return _instance;
    }

    private RandomCreatorService() {}

    /**
     * Generates a random string.
     * @return The generated string.
     */
    public String generateString() {
        byte[] array = new byte[this._random.nextInt(128*4)];
        this._random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
