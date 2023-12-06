package edu.uoc.epcsd.communication.domain.service;



import edu.uoc.epcsd.communication.domain.Property;
import edu.uoc.epcsd.communication.domain.model.Notification;
import edu.uoc.epcsd.communication.domain.model.MessageStatus;
import edu.uoc.epcsd.communication.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.uoc.epcsd.communication.domain.User;

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
        sendEmail(user.getEmail(), "PRECIO JUSTO - Registro Confirmado",
                "Gracias por registrarte en nuestra plataforma.");

        saveNotification("Usuario registrado con éxito: " + user.getEmail(), user.getId());

        return true;
    }

    @Override
    public boolean notifyPropertyValidated(Property property) {
        sendEmail(property.getContact(), "PRECIO JUSTO - Propiedad validada correctamente",
                "La propiedad " + property.getCatastralReference() + " se ha agregado a su lista de propiedades");

        saveNotification("Propiedad validada : " + property.getId(), property.getUserId());

        return true;
    }

    @Override
    public boolean notifyPropertyDeleted(Property property) {
        sendEmail(property.getContact(), "PRECIO JUSTO - Propiedad eliminada",
                "La propiedad " + property.getCatastralReference() + " se ha eliminado de su lista de propiedades");

        saveNotification("Propiedad eliminada : " + property.getId(), property.getUserId());

        return true;
    }

    @Override
    public boolean notifyChangeRequest(Property property){
        sendEmail(property.getContact(), "PRECIO JUSTO - Solicitud cambio en propiedad",
                "La propiedad " + property.getCatastralReference() + " ha recibido una petición de alta nueva," +
                        " si ya no es usted el propietario, porfavor tramite la baja en su lista de propiedades");

        saveNotification("Propiedad peticion cambio : " + property.getId(), property.getUserId());

        return true;
    }
    @Override
    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll().stream().collect(Collectors.toList());
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private void saveNotification(String content, Long userId) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setSendDate(LocalDateTime.now());
        notification.setStatus(MessageStatus.SENT);
        notification.setUserId(userId);
        notificationRepository.save(notification);
    }

}
