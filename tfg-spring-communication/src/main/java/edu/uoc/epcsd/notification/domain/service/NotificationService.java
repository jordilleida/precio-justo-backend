package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.domain.User;
import edu.uoc.epcsd.notification.domain.model.Notification;

import java.util.List;

public interface NotificationService {
    boolean notifyUserRegistered(User user);
    List<Notification> findAllNotifications();

}
