package edu.uod.tfg.property.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataOwnerHistoryRepository extends JpaRepository<OwnerHistoryEntity, Long> {
    Optional<OwnerHistoryEntity> findByUserIdAndProperty_Id(Long userId, Long propertyId);
}
