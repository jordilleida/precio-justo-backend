package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataPropertyRepository extends JpaRepository<PropertyEntity, Long> {
    List<PropertyEntity> findByUserIdAndStatus(Long userId, String status);
    Optional<PropertyEntity> findByCatastralReference(String reference);

    List<PropertyEntity> findAllByStatus(String status);
}
