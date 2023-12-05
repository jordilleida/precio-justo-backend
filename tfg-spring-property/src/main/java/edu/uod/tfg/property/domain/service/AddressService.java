package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.*;

import java.util.Optional;

public interface AddressService {
    Country addCountry(Country country);
    City addCity(City city);
    PostalCode addPostalCode(PostalCode postalCode);
    Region addRegion(Region region);

    Property saveCompleteAddress(Property property);
    Optional<Country> getCountry(String name);
    Optional<City> getCity(String name);
    Optional<Region> getRegion(String name);
    Optional<PostalCode> getPostalCode(String name);

}
