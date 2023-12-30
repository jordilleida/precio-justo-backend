package edu.uoc.epcsd.communication.application.rest;

import edu.uoc.epcsd.communication.application.request.NewMessageRequest;
import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.model.MessageStatus;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.service.MessageService;
import edu.uoc.epcsd.communication.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class CommunicationController {
    private final NotificationService notificationService;
    private final MessageService messageService;
    @GetMapping("notifications")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.findAllNotifications();
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }
    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestBody NewMessageRequest messageRequest) {

        Message message = new Message(
                null,
                messageRequest.getSenderId(),
                messageRequest.getReceiverId(),
                messageRequest.getContent(),
                LocalDateTime.now(),
                messageRequest.getAnswerTo(),
                MessageStatus.SENT
        );

        boolean isSent = messageService.sendMessage(message);

        if (isSent) {
            return ResponseEntity.ok("Mensaje enviado con éxito");
        } else {
            return ResponseEntity.unprocessableEntity().body("No se ha podido enviar el mensaje");
        }
    }
    @GetMapping("/messages/{userId}")
    public ResponseEntity<List<Message>> getMessagesInitializedBy(@PathVariable Long userId) {
        List<Message> messages = messageService.findAllMessagesInicializedBy(userId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/replies/{initialMessageId}")
    public ResponseEntity<List<Message>> getMessageReplies(@PathVariable Long initialMessageId) {

        List<Message> replies = messageService.findAllReplies(initialMessageId);

        if (replies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(replies);
    }
}
