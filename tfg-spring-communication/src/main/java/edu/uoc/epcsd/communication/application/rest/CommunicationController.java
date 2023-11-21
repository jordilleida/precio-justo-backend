package edu.uoc.epcsd.communication.application.rest;

import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.service.MessageService;
import edu.uoc.epcsd.communication.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/communication")
public class CommunicationController {
    private final NotificationService notificationService;
    private final MessageService messageService;
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.findAllNotifications();
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        boolean isSent = messageService.sendMessage(message);
        if (isSent) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
}
