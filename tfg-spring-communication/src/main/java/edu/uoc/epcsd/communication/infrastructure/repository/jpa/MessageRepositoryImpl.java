package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<Message> findUserMessages(Long id){
        return messageJpaRepository.findBySenderIdAndAnswerToIsNull(id)
                                   .stream().map(MessageEntity::toDomain).collect(Collectors.toList());
    }

}