package edu.uoc.epcsd.notification.application.rest;

import edu.uoc.epcsd.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

}
