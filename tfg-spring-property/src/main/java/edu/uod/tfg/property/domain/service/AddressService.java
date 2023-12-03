package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.*;

public interface AddressService {
    Long addCountry(Country country);
    Long addCity(City city);
    Long addPostalCode(PostalCode postalCode);
    Long addRegion(Region region);

    Boolean saveCompleteAddress(Property property);
    Country getCountry(String name);
    City getCity(String name);
    Region getRegion(String name);
    PostalCode getPostalCode(String name);

}
