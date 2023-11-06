package edu.uoc.epcsd.user.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.uoc.epcsd.user.domain.User;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable{

    private User user;
    private LocalDateTime expireDate;
    
    public String getString() {
    	return "{" +
    			"\"mail\":\"" + user.getEmail() +
    			"\", \"roles\":\"" + user.getRoles() +
    			"\", \"expire\":\"" + expireDate.format(DateTimeFormatter.ISO_DATE_TIME)+
    			"\"}";
    }
}
