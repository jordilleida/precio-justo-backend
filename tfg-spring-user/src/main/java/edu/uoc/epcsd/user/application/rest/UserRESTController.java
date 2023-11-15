package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.UserNotFoundException;

import edu.uoc.epcsd.user.application.request.*;
import edu.uoc.epcsd.user.domain.*;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.UserSession;
import edu.uoc.epcsd.user.domain.service.TokenService;
import edu.uoc.epcsd.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class UserRESTController {

    private final UserService userService;
    private final TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.findUserByMail(loginRequest.getMail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password.");
        }

        User user = userOptional.get();
        if (user.getPassword().equals(loginRequest.getPassword())) {
            UserSession session = new UserSession();
            session.setUser(user);
            session.setExpireDate(LocalDateTime.now().plusDays(1));

            try {
                String token = tokenService.encrypt(session);
                return ResponseEntity.ok().body(token);
            } catch (Exception e) {
                log.warn("Login exception encrypt " + e.getMessage() + " " + LocalDateTime.now());
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest logoutRequest) {

        log.info("User logged out: " + logoutRequest.getUserId());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/register")
    public ResponseEntity<Long> Register(@Valid @RequestBody RegisterRequest registerRequest) {

        Optional<User> existingUser = userService.findUserByMail(registerRequest.getMail());

        if (existingUser.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }

        //el rol del usuario por defecto es Buyer
        Set<Role> roles = new HashSet<>();
                 roles.add(new Role(1L, Rol.BUYER));

        User user = new User(null, registerRequest.getName(), registerRequest.getSurname(),
                registerRequest.getMail(), registerRequest.getPassword(), roles, false);

        log.info("createUser " + registerRequest.getMail());

        Long userId = userService.createUser(user);

    	return ResponseEntity.ok().body(userId);
    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers() {

        return userService.findAllUsers();
    }

    @GetMapping("/users/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {

        return userService.findUserByMail(email).map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        log.info("deleteUser " + id);

        userService.deleteUser(id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleUserNotFound(UserNotFoundException userNotFoundException) {
        return userNotFoundException.getMessage();
    }
}
