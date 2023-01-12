package laustrup.bandwichpersistencedebugging.models.users.contact_infos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Details about phone contacting information.
 */
@NoArgsConstructor @ToString
public class Phone {

    /**
     * A country object, that represents the nationality of this PhoneNumber.
     */
    @Getter @Setter
    private Country _country;

    /**
     * The contact numbers for the Phone.
     */
    @Getter @Setter
    private long _numbers;

    /**
     * True if the number is for a mobile.
     */
    @Getter
    private boolean _mobile;

    public Phone(Country country, long numbers, boolean mobile) {
        _country = country;
        _numbers = numbers;
        _mobile = mobile;
    }
}
