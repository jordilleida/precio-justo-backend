package edu.uoc.epcsd.user.domain.messaging;

import edu.uoc.epcsd.user.domain.User;

public interface MessageService {
    void sendMessage(User user);
}
