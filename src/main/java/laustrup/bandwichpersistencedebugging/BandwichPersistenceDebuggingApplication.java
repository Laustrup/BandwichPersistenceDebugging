package laustrup.bandwichpersistencedebugging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BandwichPersistenceDebuggingApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BandwichPersistenceDebuggingApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BandwichPersistenceDebuggingApplication.class, args);
    }

}