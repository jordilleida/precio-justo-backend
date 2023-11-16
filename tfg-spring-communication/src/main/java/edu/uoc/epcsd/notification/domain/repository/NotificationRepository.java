package edu.uoc.epcsd.notification.domain.repository;

import edu.uoc.epcsd.notification.domain.model.Notification;
import edu.uoc.epcsd.notification.infrastructure.repository.jpa.NotificationEntity;

import java.util.List;

public interface NotificationRepository {

    void save(Notification notification);
    List<Notification> findByUserId(Long userId);
    List<Notification> findAll();
}
