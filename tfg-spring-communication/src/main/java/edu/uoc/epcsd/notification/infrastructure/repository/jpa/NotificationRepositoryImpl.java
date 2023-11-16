package edu.uoc.epcsd.notification.infrastructure.repository.jpa;

import edu.uoc.epcsd.notification.domain.model.Notification;
import edu.uoc.epcsd.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationRepositoryImpl implements NotificationRepository {

    private final SpringDataNotificationRepository notificationJpaRepository;

    public List<Notification> findByUserId(Long id){
        return notificationJpaRepository.findByUserId(id).stream()
                                .map(NotificationEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public void save(Notification notification) {
                notificationJpaRepository.save(NotificationEntity.fromDomain(notification));
    }

    @Override
    public List<Notification> findAll() {
        return notificationJpaRepository.findAll().stream()
                .map(NotificationEntity::toDomain)
                .collect(Collectors.toList());
    }
}
