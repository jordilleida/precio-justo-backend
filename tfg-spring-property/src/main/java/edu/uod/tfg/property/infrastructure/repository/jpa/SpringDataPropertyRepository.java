package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPropertyRepository extends JpaRepository<PropertyEntity, Long> {
}
