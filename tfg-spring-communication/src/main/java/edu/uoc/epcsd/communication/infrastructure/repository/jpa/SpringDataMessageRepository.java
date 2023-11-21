package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SpringDataMessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderIdAndAnswerToIsNull(Long senderId);

    @Query("SELECT m FROM MessageEntity m WHERE (m.senderId = :userId OR m.receiverId = :userId) AND m.answerTo IS NULL")
    List<MessageEntity> findInitialMessagesByUserId(Long userId);
    List<MessageEntity> findByAnswerTo(Long messageId);

    @Query("SELECT m FROM MessageEntity m WHERE m.answerTo IN (SELECT m.id FROM MessageEntity m WHERE m.senderId = :senderId)")
    List<MessageEntity> findAllRepliesForSenderId(Long senderId);
}