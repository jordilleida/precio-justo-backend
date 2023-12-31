package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);
    
    Optional<User> findUserByMail(String email);

    User createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);

}
