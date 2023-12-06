package edu.uoc.epcsd.communication.domain.service;

import edu.uoc.epcsd.communication.domain.Property;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.User;

import java.util.List;

public interface NotificationService {
    boolean notifyUserRegistered(User user);
    List<Notification> findAllNotifications();

    boolean notifyPropertyValidated(Property property);
    boolean notifyPropertyDeleted(Property property);
    boolean notifyChangeRequest(Property property);
}
