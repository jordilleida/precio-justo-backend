package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCountryRepository extends JpaRepository<CountryEntity, Long> {

 Optional<CountryEntity> findByName(String name);
}
