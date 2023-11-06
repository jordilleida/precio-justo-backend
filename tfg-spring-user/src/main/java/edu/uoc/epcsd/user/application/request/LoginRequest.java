package edu.uoc.epcsd.user.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class LoginRequest {

    @Getter
    @NotNull
    private final String mail;

    @Getter
    @NotNull
    private final String password;

    @JsonCreator
    public LoginRequest(@JsonProperty("mail") @NotNull final String mail, @JsonProperty("password") @NotNull final String password) {
        this.mail = mail;
        this.password = password;
    }
}
