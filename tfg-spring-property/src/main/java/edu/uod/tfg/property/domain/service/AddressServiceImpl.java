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
    public Property saveCompleteAddress(Property property) {

        PostalCode postalCode = property.getPostalCode();
        City city = postalCode.getCity();
        Region region = city.getRegion();
        Country country = region.getCountry();

        // Guardar o verificar el país
        country = addCountry(country); //Con la id

        // Guardar o verificar la región

        region.setCountry(country);
        region = addRegion(region); //Con la id

        // Guardar o verificar la ciudad
        city.setRegion(region);
        city = addCity(city); //Con la id

        // Guardar o verificar el código postal
        postalCode.setCity(city);
        postalCode = addPostalCode(postalCode); //Con el identificador insertado

        // Establecemos el id del código postal guardado en la propiedad para ver si ha ido bien
        property.setPostalCode(postalCode);

        return property;
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
