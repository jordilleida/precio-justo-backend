package edu.uoc.epcsd.notification.domain.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.uoc.epcsd.notification.domain.User;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationServiceImpl implements NotificationService {

    // Inyecta JavaMailSender si vas a enviar correos electrónicos
    // Inyecta tu repositorio de notificaciones
    @Override
    public boolean notifyUserRegistered(User user) {
        return true;
    }
}
