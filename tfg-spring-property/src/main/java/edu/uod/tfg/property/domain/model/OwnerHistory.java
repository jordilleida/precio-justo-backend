package edu.uod.tfg.property.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerHistory {
    private Long id;

    private Long userId;
    private Property property;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
