package edu.uoc.epcsd.unitTest;


import edu.uoc.epcsd.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import edu.uoc.epcsd.user.domain.repository.UserRepository;
import edu.uoc.epcsd.user.domain.service.UserService;
import edu.uoc.epcsd.user.domain.service.UserServiceImpl;
import edu.uoc.epcsd.user.infrastructure.repository.jpa.UserRepositoryImpl;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootConfiguration
@SpringBootTest
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("FindAllUsers")
    void FindAllUsers() {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        given(userRepository.findAllUsers()).willReturn(users);

        List<User> expectedUsers = userService.findAllUsers();
        assertThat(expectedUsers).isEqualTo(users);
    }

    @Test
    @DisplayName("FindUserById")
    void FindUserById_ok() {

        final long userId = 1L;
        User user = new User();

        given(userRepository.findUserById(userId)).willReturn(Optional.ofNullable(user));
        Optional<User> expectedUser = userService.findUserById(userId);

        assertThat(expectedUser).isNotNull();
    }

    @Test
    @DisplayName("CreateUser")
    void CreateUser() {
        // Creo una instancia de User para la prueba
        User user = new User();

        // Creo un usuario mock con el ID esperado
        User mockUser = new User();
        mockUser.setId(1L);

        given(userRepository.createOrEditUser(any(User.class))).willReturn(mockUser);

        Long newUserId = userService.createUser(user);

        // Valido que el ID del usuario retornado es 1L
        assertThat(newUserId).isNotNull();
        assertThat(newUserId).isEqualTo(1L);

        // Verifico las interacciones con el mock
        verify(userRepository).createOrEditUser(any(User.class));
        verify(userRepository, times(1)).createOrEditUser(user);
    }
    @Test
    @DisplayName("DeleteUser")
    void DeleteUser() {

        final long userId = 1L;

        userService.deleteUser(userId);
        userService.deleteUser(userId);
        userService.deleteUser(userId);

        verify(userRepository, times(3)).deleteUser(userId);
    }
}
