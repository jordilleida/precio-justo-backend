package edu.uoc.epcsd.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import edu.uoc.epcsd.communication.domain.model.Message;
import edu.uoc.epcsd.communication.domain.repository.MessageRepository;
import edu.uoc.epcsd.communication.domain.service.MessageServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setContent("TEST DE MENSAJES");
        testMessage.setSenderId(1L);
        testMessage.setReceiverId(2L);
        testMessage.setAnswerTo(null);

    }

    @Test
    @DisplayName("Send Message")
    void sendMessage() {
        assertTrue(messageService.sendMessage(testMessage));
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Find All Messages Initialized By User")
    void findAllMessagesInitializedBy() {
        List<Message> messages = new ArrayList<>();
        messages.add(testMessage);

        when(messageRepository.findUserMessages(1L)).thenReturn(messages);

        List<Message> returnedMessages = messageService.findAllMessagesInicializedBy(1L);
        assertEquals(messages.size(), returnedMessages.size());
        assertEquals(testMessage.getContent(), returnedMessages.get(0).getContent());
    }

    @Test
    @DisplayName("Find All Messages Initialized By User - Empty List")
    void findAllMessagesInitializedByEmpty() {
        when(messageRepository.findUserMessages(1L)).thenReturn(Collections.emptyList());

        List<Message> returnedMessages = messageService.findAllMessagesInicializedBy(1L);
        assertTrue(returnedMessages.isEmpty());
    }
    @Test
    @DisplayName("Find All Replies")
    void findAllReplies() {
        Message replyMessage = new Message();
        replyMessage.setId(2L);
        replyMessage.setContent("TEST DE MENSAJES");
        replyMessage.setSenderId(3L);
        replyMessage.setReceiverId(2L);
        replyMessage.setAnswerTo(1L);

        when(messageRepository.findByAnswerTo(1L)).thenReturn(Optional.of(replyMessage));
        when(messageRepository.findByAnswerTo(2L)).thenReturn(Optional.empty());

        List<Message> expectedReplies = new ArrayList<>();
        expectedReplies.add(replyMessage);

        assertEquals(expectedReplies, messageService.findAllReplies(1L));
    }

}
