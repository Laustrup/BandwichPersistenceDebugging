package laustrup.bandwichpersistencedebugging.models.users.contact_infos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains values that determines address attributes.
 */
@NoArgsConstructor
public class Address {

    /**
     * The street and street number.
     */
    @Getter @Setter
    private String _street;

    /**
     * The floor, if in an apartment, also include left or right.
     */
    @Getter @Setter
    private String _floor;

    /**
     * Some digits describing the city.
     */
    @Getter @Setter
    private String _postal;

    /**
     * The city of the postal.
     */
    @Getter @Setter
    private String _city;

    public Address(String street, String floor, String postal, String city) {
        _street = street;
        _floor = floor;
        _postal = postal;
        _city = city;
    }

    @Override
    public String toString() {
        String info = new String();

        info += get_street() != null ? get_street() + ", " : "";
        info += get_floor() != null ? get_floor() + ", " : "";
        info += get_postal() != null ? get_postal() + " " : "";
        info += get_city() != null ? get_city() : "";

        return info;
    }
}
