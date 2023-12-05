package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import edu.uoc.epcsd.user.infrastructure.kafka.KafkaUserMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final KafkaUserMessagingService kafkaUserMessagingService;
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<User> findUserByMail(String email) {

        return userRepository.findUserByMail(email);
    }
    @Override
    public Long createUser(User user) {

        User newUser = userRepository.createOrEditUser(user);

        kafkaUserMessagingService.sendMessage(newUser);

        return newUser.getId();
    }

    @Override
    public void updateUser(User user) {

        userRepository.createOrEditUser(user);

    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }
}
