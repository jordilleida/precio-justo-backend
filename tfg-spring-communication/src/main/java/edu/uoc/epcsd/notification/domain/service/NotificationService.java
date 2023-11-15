package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.domain.User;

public interface NotificationService {
    boolean notifyUserRegistered(User user);

}
