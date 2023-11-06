package edu.uoc.epcsd.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;

import edu.uoc.epcsd.notification.domain.Category;
import edu.uoc.epcsd.notification.domain.Show;
import edu.uoc.epcsd.notification.domain.User;
import edu.uoc.epcsd.notification.domain.service.NotificationServiceImpl;
import edu.uoc.epcsd.notification.domain.service.UserService;

@SpringBootTest
class NotificationServiceTests {
    @Mock
    private UserService userService;
    @InjectMocks
    private NotificationServiceImpl notifyService;
    @Test
    @DisplayName("Send notification by Show")
    void NotifyByShow() {
    	Category cat = Category.builder().id(1L).name("testCategory").build();
		Show showTest = Show.builder()
    			.capacity(1L)
    			.category(cat)
    			.name("Test")
    			.build();
    	Set<User> users = new HashSet<User>();
    	users.add(User.builder().email("test@test.com").fullName("Test user").id(1L).build());
    	
    	given(userService.getUsersByFavouriteCategory(cat)).willReturn(users);
    	Boolean result = notifyService.notifyShowCreation(showTest);
    	assertEquals(result, true);
    }
}
