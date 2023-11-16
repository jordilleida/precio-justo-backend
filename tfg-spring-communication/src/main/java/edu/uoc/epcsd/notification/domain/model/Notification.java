package edu.uoc.epcsd.notification.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;

    private Long userId;
    private String content;
    private LocalDateTime sendDate;
    private MessageStatus status;
}
