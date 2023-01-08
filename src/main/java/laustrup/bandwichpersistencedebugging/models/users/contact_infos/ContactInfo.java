package laustrup.bandwichpersistencedebugging.models.users.contact_infos;

import laustrup.bandwichpersistencedebugging.models.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Contains information that people need in order to contact the User.
 */
@ToString
public class ContactInfo extends Model {

    /**
     * The email that the User wants to be contacted through outside the application.
     */
    @Getter @Setter
    private String _email;

    /**
     * A Phone object that is used to have information about how to contact the User through Phone.
     */
    @Getter
    private Phone _phone;

    /**
     * An Address object with info about the location of the User.
     */
    @Getter @Setter
    private Address _address;

    /**
     * A Country object for the information of which Country the User is living in.
     */
    @Getter
    private Country _country;

    public ContactInfo(long id, String email, Phone phone, Address address, Country country, LocalDateTime timestamp) {
        super(id, "Contact-info: "+id, timestamp);
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
    }

    public ContactInfo(String email, Phone phone, Address address, Country country) {
        super();
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
        _title = getAddressInfo();
    }

    /**
     * Collects the details of the Address as a one liner String.
     * @return The collected one liner String of the Address.
     */
    public String getAddressInfo() {
        String info = new String();

        info += _address.get_street() != null ? _address.get_street() + ", " : "";
        info += _address.get_floor() != null ? _address.get_floor() + ", " : "";
        info += _address.get_postal() != null ? _address.get_postal() + " " : "";
        info += _address.get_city() != null ? _address.get_city() : "";

        return info;
    }
}
