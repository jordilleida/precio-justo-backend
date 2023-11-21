package edu.uoc.epcsd.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import edu.uoc.epcsd.communication.domain.User;
import edu.uoc.epcsd.communication.domain.service.NotificationServiceImpl;

@SpringBootTest
class NotificationServiceTests {
    @InjectMocks
    private NotificationServiceImpl notifyService;
    @Test
    @DisplayName("Send notification by User")
    void NotifyByUser() {

    	User user = User.builder().email("test@test.com").fullName("Test user").id(1L).build();

    	Boolean result = notifyService.notifyUserRegistered(user);
    	assertEquals(result, true);
    }
}
