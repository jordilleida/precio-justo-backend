package edu.uoc.epcsd.communication.domain.repository;

import edu.uoc.epcsd.communication.domain.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    void save(Message message);
    List<Message> findUserMessages(Long userId);
    Optional<Message> findByAnswerTo(Long messageId);
}
