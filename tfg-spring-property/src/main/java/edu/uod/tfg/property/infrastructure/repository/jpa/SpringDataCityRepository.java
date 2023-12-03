package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCityRepository extends JpaRepository<CityEntity, Long> {

    Optional<CityEntity> findByName(String name);
}
