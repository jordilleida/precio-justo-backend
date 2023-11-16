package edu.uoc.epcsd.notification.domain.service;



import edu.uoc.epcsd.notification.domain.model.MessageStatus;
import edu.uoc.epcsd.notification.domain.model.Notification;
import edu.uoc.epcsd.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.uoc.epcsd.notification.domain.User;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    @Override
    public boolean notifyUserRegistered(User user) {

        // Envío de correo electrónico
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Registro Confirmado");
        message.setText("PRECIO JUSTO. Gracias por registrarte en nuestra plataforma.");
        mailSender.send(message);

        // Guardar en la base de datos
        Notification notification = new Notification();
        notification.setContent("Usuario registrado con éxito");
        notification.setSendDate(LocalDateTime.now());
        notification.setStatus(MessageStatus.SENT);
        notificationRepository.save(notification);

        return true;
    }

    @Override
    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll().stream().collect(Collectors.toList());
    }
}
