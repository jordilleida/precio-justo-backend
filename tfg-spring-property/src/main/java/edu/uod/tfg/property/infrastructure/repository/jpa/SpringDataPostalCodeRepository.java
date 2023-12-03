package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPostalCodeRepository extends JpaRepository<PostalCodeEntity, Long> {
    Optional<PostalCodeEntity> findByCode(String code);
}
