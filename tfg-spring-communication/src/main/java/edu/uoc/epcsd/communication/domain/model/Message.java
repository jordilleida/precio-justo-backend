package edu.uoc.epcsd.communication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;

    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime sendDate;
    private Long answerTo;
    private MessageStatus status;
}
