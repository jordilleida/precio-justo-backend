package edu.uod.tfg.property.domain.repository;

import edu.uod.tfg.property.domain.model.City;
import edu.uod.tfg.property.domain.model.Country;
import edu.uod.tfg.property.domain.model.PostalCode;
import edu.uod.tfg.property.domain.model.Region;

import java.util.Optional;

public interface AddressRepository {
    Country addCountry(Country country);
    City addCity(City city);
    PostalCode addPostalCode(PostalCode postalCode);
    Region addRegion(Region region);

    Optional<Country> getCountry(String name);
    Optional<City> getCity(String name);
    Optional<Region> getRegion(String name);
    Optional<PostalCode> getPostalCode(String name);
}
