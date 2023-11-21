package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import edu.uoc.epcsd.communication.domain.model.MessageStatus;
import edu.uoc.epcsd.communication.domain.model.Notification;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity implements DomainTranslatable<Notification> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    public static NotificationEntity fromDomain(Notification notification) {

        if (notification == null) return null;

        return NotificationEntity.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .content(notification.getContent())
                .sendDate(notification.getSendDate())
                .status(notification.getStatus())
                .build();
    }

    @Override
    public Notification toDomain() {
        return new Notification(this.id, this.userId, this.content, this.sendDate, this.status);
    }
}
