package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpringDataMessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE (m.senderId = :userId OR m.receiverId = :userId) AND m.answerTo IS NULL")
    List<MessageEntity> findInitialMessagesByUserId(Long userId);
    Optional<MessageEntity> findByAnswerTo(Long messageId);

    @Query("SELECT m FROM MessageEntity m WHERE m.answerTo IN (SELECT m.id FROM MessageEntity m WHERE m.senderId = :senderId)")
    List<MessageEntity> findAllRepliesForSenderId(Long senderId);
}