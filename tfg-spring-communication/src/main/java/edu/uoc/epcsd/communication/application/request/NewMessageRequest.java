package edu.uoc.epcsd.communication.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class NewMessageRequest {

    @Getter
    @NotNull
    private final Long senderId;

    @Getter
    @NotNull
    private final Long receiverId;

    @Getter
    @NotNull
    private final String content;

    @Getter
    private final Long answerTo;

    @JsonCreator
    public NewMessageRequest(
            @JsonProperty("senderId") final Long senderId,
            @JsonProperty("receiverId") final Long receiverId,
            @JsonProperty("content") final String content,
            @JsonProperty("answerTo") final Long answerTo) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.answerTo = answerTo;
    }
}
