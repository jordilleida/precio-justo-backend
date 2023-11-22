package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository userJpaRepository;
    @Override
    public List<User> findAllUsers() {
        return userJpaRepository.findAll().stream().map(UserEntity::toDomain).collect(Collectors.toList());
    }
    @Override
    public Optional<User> findUserById(Long id) {
        return userJpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findUserByMail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }
    @Override
    public User createOrEditUser(User user) {
        UserEntity newUser =  userJpaRepository.save(UserEntity.fromDomain(user));

        return newUser.toDomain();
    }
    @Override
    public void deleteUser(Long id) {
        userJpaRepository.deleteById(id);
    }
}
