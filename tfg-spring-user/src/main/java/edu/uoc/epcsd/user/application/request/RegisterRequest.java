package edu.uoc.epcsd.user.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterRequest {

    @Getter
    @NotNull
    private final String name;

    @Getter
    @NotNull
    private final String surname;

    @Getter
    @NotNull
    @Email(message = "Email should be valid")
    private final String mail;
    @Getter
    @NotNull
    @NotBlank(message = "Password cannot be blank")
    private final String password;

    
    @JsonCreator
    public RegisterRequest(@JsonProperty("name") @NotNull final String name, @JsonProperty("surname") @NotNull final String surname,
    @JsonProperty("mail") @NotNull final String mail, @JsonProperty("password") @NotNull final String password) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
    }
}
