package edu.uoc.epcsd.communication.domain.repository;

import edu.uoc.epcsd.communication.domain.model.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    List<Message> findUserMessages(Long id);
}
