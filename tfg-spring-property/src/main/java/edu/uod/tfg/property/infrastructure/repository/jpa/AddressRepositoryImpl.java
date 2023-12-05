package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.City;
import edu.uod.tfg.property.domain.model.Country;
import edu.uod.tfg.property.domain.model.PostalCode;
import edu.uod.tfg.property.domain.model.Region;
import edu.uod.tfg.property.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressRepositoryImpl implements AddressRepository {


    private final SpringDataCountryRepository countryJpaRepository;

    private final SpringDataCityRepository cityJpaRepository;

    private final SpringDataPostalCodeRepository postalCodeJpaRepository;

    private final SpringDataRegionRepository regionJpaRepository;

    @Override
    public Country addCountry(Country country) {
        Optional<CountryEntity> existingCountry = countryJpaRepository.findByName(country.getName());
        if (existingCountry.isPresent()) {
            return existingCountry.get().toDomain();
        } else {

            CountryEntity entity = CountryEntity.fromDomain(country);
            entity = countryJpaRepository.save(entity);
            return entity.toDomain();
        }
    }

    @Override
    public City addCity(City city) {
        Optional<CityEntity> existingCity = cityJpaRepository.findByName(city.getName());

        if (existingCity.isPresent())
        {
            return existingCity.get().toDomain();
        }
        else
        {
            CityEntity entity = CityEntity.fromDomain(city);
            entity = cityJpaRepository.save(entity);
            return entity.toDomain();
        }
    }

    @Override
    public PostalCode addPostalCode(PostalCode postalCode) {

        Optional<PostalCodeEntity> existingPostalCode = postalCodeJpaRepository.findByCode(postalCode.getCode());
        if (existingPostalCode.isPresent()) {

            return existingPostalCode.get().toDomain();
        } else {

            PostalCodeEntity entity = PostalCodeEntity.fromDomain(postalCode);
            entity = postalCodeJpaRepository.save(entity);
            return entity.toDomain();
        }
    }

    @Override
    public Region addRegion(Region region) {


        Optional<RegionEntity> existingRegion = regionJpaRepository.findByName(region.getName());
        if (existingRegion.isPresent()) {

            return existingRegion.get().toDomain();
        } else {
            CountryEntity countryEntity = countryJpaRepository.findByName(region.getCountry().getName())
                    .orElseGet(() -> countryJpaRepository.save(CountryEntity.fromDomain(new Country(null, region.getCountry().getName()))));

            RegionEntity entity = RegionEntity.fromDomain(region);
            entity.setCountry(countryEntity);
            entity = regionJpaRepository.save(entity);
            return entity.toDomain();
        }
    }

    @Override
    public Optional<Country> getCountry(String name) {
        return countryJpaRepository.findByName(name).map(CountryEntity::toDomain);
    }

    @Override
    public Optional<City> getCity(String name) {
        return cityJpaRepository.findByName(name).map(CityEntity::toDomain);
    }

    @Override
    public Optional<Region> getRegion(String name) {
        return regionJpaRepository.findByName(name).map(RegionEntity::toDomain);
    }

    @Override
    public Optional<PostalCode> getPostalCode(String code) {
        return postalCodeJpaRepository.findByCode(code).map(PostalCodeEntity::toDomain);
    }
}
