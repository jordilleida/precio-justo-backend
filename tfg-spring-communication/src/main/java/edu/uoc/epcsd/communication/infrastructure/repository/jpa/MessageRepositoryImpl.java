package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageRepositoryImpl implements MessageRepository {

    private final SpringDataMessageRepository messageJpaRepository;

    @Override
    public void save(Message message) {
        messageJpaRepository.save(MessageEntity.fromDomain(message));
    }

    @Override
    public List<Message> findUserMessages(Long userId){
        return messageJpaRepository.findInitialMessagesByUserId(userId)
                                   .stream().map(MessageEntity::toDomain).collect(Collectors.toList());
    }
    @Override
    public Optional<Message> findByAnswerTo(Long messageId){
        return messageJpaRepository.findByAnswerTo(messageId)
                .map(MessageEntity::toDomain);
    }

}