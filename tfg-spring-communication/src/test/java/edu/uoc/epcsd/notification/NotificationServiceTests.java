package edu.uoc.epcsd.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import edu.uoc.epcsd.communication.domain.User;
import edu.uoc.epcsd.communication.domain.Property;
import edu.uoc.epcsd.communication.domain.repository.NotificationRepository;
import edu.uoc.epcsd.communication.domain.service.NotificationServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTests {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User testUser;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setId(1L);

        testProperty = new Property();
        testProperty.setContact("contact@example.com");
        testProperty.setCatastralReference("Ref1234");
        testProperty.setId(2L);
        testProperty.setUserId(1L);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Notify User Registered")
    void notifyUserRegistered() {
        assertTrue(notificationService.notifyUserRegistered(testUser));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Notify Property Validated")
    void notifyPropertyValidated() {
        assertTrue(notificationService.notifyPropertyValidated(testProperty));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Notify Property Deleted")
    void notifyPropertyDeleted() {
        assertTrue(notificationService.notifyPropertyDeleted(testProperty));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Notify Change Request")
    void notifyChangeRequest() {
        assertTrue(notificationService.notifyChangeRequest(testProperty));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

