package edu.uoc.epcsd.communication.domain.service;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MessageServiceImpl implements  MessageService{

    private final MessageRepository messageRepository;

    @Override
    public boolean sendMessage(Message message) {
                 messageRepository.save(message);
        return true;
    }

    @Override
    public List<Message> findAllMessagesInicializedBy(Long userId) {
        return messageRepository.findUserMessages(userId);
    }
}
