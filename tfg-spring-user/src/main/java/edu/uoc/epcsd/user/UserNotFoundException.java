package edu.uoc.epcsd.user;

public class UserNotFoundException extends RuntimeException {

    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("Not found user by id: " + userId);
        this.userId = userId;
    }
}
