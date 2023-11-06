package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);
    
    Optional<User> findUserByMail(String email);

    Long createUser(User user);

    void deleteUser(Long id);

}
