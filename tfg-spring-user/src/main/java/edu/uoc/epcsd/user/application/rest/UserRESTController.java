package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.UserNotFoundException;

import edu.uoc.epcsd.user.application.request.*;
import edu.uoc.epcsd.user.domain.*;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.UserSession;
import edu.uoc.epcsd.user.domain.service.RoleService;
import edu.uoc.epcsd.user.domain.service.TokenService;
import edu.uoc.epcsd.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RoleService roleService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.findUserByMail(loginRequest.getMail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserSession session = new UserSession();
        session.setUser(user);
        session.setExpireDate(LocalDateTime.now().plusDays(1));

        try {
            String accessToken = tokenService.createToken(session);
            String idToken = tokenService.createIdToken(session);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("idToken", idToken);

            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            log.warn("Login exception encrypt " + e.getMessage() + " " + LocalDateTime.now());
            return ResponseEntity.internalServerError().build();
        }
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
        Role defaultRole = roleService.getDefaultRole();

        Set<Role> roles = new HashSet<>();
                  roles.add(defaultRole);

        User user = new User(null, registerRequest.getName(), registerRequest.getSurname(),
                registerRequest.getMail(), registerRequest.getPassword(), roles, false);

        Long userId = userService.createUser(user);

        log.info("createUser " + registerRequest.getMail());

    	return ResponseEntity.ok().body(userId);
    }

    @PutMapping("/users/{userId}/add-seller")
    public ResponseEntity<?> addSellerRoleToUser(@PathVariable Long userId) {
        try {
            Optional<User> userOptional = userService.findUserById(userId);

            if (userOptional.isEmpty()) {
                log.info("User not found: " + userId);
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            Role sellerRole = roleService.getSellerRole();

            if (user.getRoles().contains(sellerRole))
                                return ResponseEntity.ok().build();

            user.getRoles().add(sellerRole);
            userService.updateUser(user);

            log.info("SELLER role added to user: " + userId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error adding SELLER role to user: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUsers() {

        return userService.findAllUsers();
    }
    @GetMapping("/roles/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Role> findRoleByName(@PathVariable String name) {

        Role role = roleService.getDefaultRole();

        return ResponseEntity.ok().body(role);

    }

    @GetMapping("/users/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {

        return userService.findUserByMail(email).map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
