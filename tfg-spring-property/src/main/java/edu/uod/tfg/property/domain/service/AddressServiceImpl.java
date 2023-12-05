package edu.uod.tfg.property.domain.service;

import edu.uod.tfg.property.domain.model.*;
import edu.uod.tfg.property.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    @Override
    public Country addCountry(Country country) { return addressRepository.addCountry(country); }
    @Override
    public City addCity(City city) { return addressRepository.addCity(city); }
    @Override
    public PostalCode addPostalCode(PostalCode postalCode) { return addressRepository.addPostalCode(postalCode); }
    @Override
    public Region addRegion(Region region) { return addressRepository.addRegion(region); }

    @Override
    public PostalCode saveCompleteAddress(String countryName, String regionName, String cityName, String postalCode) {
        // Crear o verificar el país
        Country country = addCountry(new Country(null, countryName));

        // Crear o verificar la región
        Region region = addRegion(new Region(null, regionName, country));

        // Crear o verificar la ciudad
        City city = addCity(new City(null, cityName, region));

        // Crear o verificar el código postal
        PostalCode postalCodeCreated = addPostalCode(new PostalCode(null, postalCode, city));


        return postalCodeCreated;
    }

    @Override
    public Optional<Country> getCountry(String name) {
        return addressRepository.getCountry(name);
    }
    @Override
    public Optional<City> getCity(String name) { return addressRepository.getCity(name); }
    @Override
    public Optional<Region> getRegion(String name) { return addressRepository.getRegion(name); }
    @Override
    public Optional<PostalCode> getPostalCode(String code) { return addressRepository.getPostalCode(code); }
}
