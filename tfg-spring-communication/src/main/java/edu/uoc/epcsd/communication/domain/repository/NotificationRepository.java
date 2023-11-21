package edu.uoc.epcsd.communication.domain.repository;

import edu.uoc.epcsd.communication.domain.model.Notification;

import java.util.List;

public interface NotificationRepository {

    void save(Notification notification);
    List<Notification> findByUserId(Long userId);
    List<Notification> findAll();
}
