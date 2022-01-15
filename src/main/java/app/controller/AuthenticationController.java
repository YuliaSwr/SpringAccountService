package app.controller;

import app.model.NewPassword;
import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/auth/signup")
    public User post(@Valid @RequestBody User user){
        return userService.save(user);
    }

    @GetMapping("/empl/payment")
    public User get(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userService.findByEmail(email);
    }

    @PostMapping("/auth/changepass")
    public Map<String, String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                              @Valid @RequestBody NewPassword newPassword) {
        return userService.changePassword(userDetails.getUsername().toLowerCase(), newPassword.getNew_password());
    }
}
