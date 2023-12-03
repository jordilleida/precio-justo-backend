package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.model.MessageStatus;
import edu.uoc.epcsd.communication.domain.model.Notification;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity implements DomainTranslatable<Message>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(nullable = false)
    private String content;

    @Column(name = "send_date")
    @Builder.Default
    private LocalDateTime sendDate = LocalDateTime.now();

    @Column(name = "reply_to_message_id")
    private Long answerTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    public static MessageEntity fromDomain(Message message) {
        if (message == null) {
            return null;
        }
        MessageEntity entity = new MessageEntity();
        entity.setId(message.getId());
        entity.setSenderId(message.getSenderId());
        entity.setReceiverId(message.getReceiverId());
        entity.setContent(message.getContent());
        entity.setSendDate(message.getSendDate());
        entity.setAnswerTo(message.getAnswerTo() != null ? message.getAnswerTo() : null);
        entity.setStatus(message.getStatus());
        return entity;
    }

    @Override
    public Message toDomain() {
        return new Message(this.id, this.senderId,
                this.receiverId,  this.content,
                this.sendDate, this.answerTo, this.status);
    }

}