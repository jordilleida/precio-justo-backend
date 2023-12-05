package edu.uod.tfg.property.infrastructure.repository.jpa;

import edu.uod.tfg.property.domain.model.OwnerHistory;
import edu.uod.tfg.property.domain.model.PostalCode;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owner_history")
public class OwnerHistoryEntity implements DomainTranslatable<OwnerHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @NotNull
    private PropertyEntity property;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "start_date")
    @NotNull
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Override
    public OwnerHistory toDomain() {
        return OwnerHistory.builder()
                .id(this.id)
                .userId(this.userId)
                .property(this.property.toDomain())
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }

    public static OwnerHistoryEntity fromDomain(OwnerHistory ownerHistory) {

        return OwnerHistoryEntity.builder()
                .id(ownerHistory.getId())
                .userId(ownerHistory.getUserId())
                .property(PropertyEntity.fromDomain(ownerHistory.getProperty()))
                .startDate(ownerHistory.getStartDate())
                .endDate(ownerHistory.getEndDate())
                .build();

    }
}
