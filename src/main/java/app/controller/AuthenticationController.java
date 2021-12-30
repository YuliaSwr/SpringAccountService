package app.controller;

import app.exception.UserExistException;
import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/auth/signup")
    public User signUp(@Valid @RequestBody User user) {
        if (userService.existsByEmailIgnoreCase(user.getEmail())) {
            throw new UserExistException("User exist!");
        }
        System.out.println("User id:" + user.getId()
                + "; email: " + user.getEmail()
                + "; password: " + user.getPassword()
        );
        user.setEnabled(true);
        userService.save(user);
        return user;
    }

    @GetMapping("/empl/payment")
    public ResponseEntity<User> authenticate(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }
}
