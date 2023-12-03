package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataRegionRepository extends JpaRepository<RegionEntity, Long> {
    Optional<RegionEntity> findByName(String name);
}
