package edu.uoc.epcsd.communication.domain.service;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.repository.MessageRepository;
import edu.uoc.epcsd.communication.infrastructure.repository.jpa.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public List<Message> findAllReplies(Long initialMessageId) {

        List<Message> replies = new ArrayList<>();
        Queue<Long> messageIdToCheck = new LinkedList<>();

        messageIdToCheck.add(initialMessageId);

        while (!messageIdToCheck.isEmpty()) {
            Long id = messageIdToCheck.remove();


            Optional<Message> reply = messageRepository.findByAnswerTo(id);

            // Si el mensaje existe, lo agregpo y pongo la id para que se verifique si hay una respuesta a ese también
            reply.ifPresent(message -> {

                replies.add(message);

                messageIdToCheck.add(message.getId());

            });
        }

        return replies;
    }
}
