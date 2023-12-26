package edu.uoc.epcsd.communication.domain.service;

import edu.uoc.epcsd.communication.domain.model.Message;

import java.util.List;

public interface MessageService {
   boolean sendMessage(Message message);
   List<Message> findAllMessagesInicializedBy(Long userId);
   List<Message> findAllReplies(Long initialMessageId);
}
