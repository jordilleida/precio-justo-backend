package edu.uoc.epcsd.notification.domain.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import edu.uoc.epcsd.notification.domain.Show;
import edu.uoc.epcsd.notification.domain.User;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;    // mock service
    @Override
    public boolean notifyShowCreation(Show show) {

        notifyNewShowUsersWithFavouriteCategory(show);
        return true;
    }

    private void notifyNewShowUsersWithFavouriteCategory(Show show) {
        for (User user : userService.getUsersByFavouriteCategory(show.getCategory())) {
            notifyUser(user, show);
        }
    }

    // mock notification
    private void notifyUser(User user, Show show) {
        // an email / push notification would be sent here
        log.info("The show \"" + show.getName() + "\" has been added!. E-mail sent to user \"" + user.getFullName() + "\"");
    }
}
