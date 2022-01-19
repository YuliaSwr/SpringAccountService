package app.service;

import app.exception.*;
import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private ArrayList<String> hackPasswords = new ArrayList<String>(Arrays.asList("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserException::new);
    }

    public User save(User user) {
        if (user.getPassword().length() < 12) {
            throw new BadPasswordException("Password length must be 12 chars minimum!");
        }

        if (hackPasswords.contains(user.getPassword())) {
            throw new BadPasswordException("The password is in the hacker's database!");
        }

        user.setEmail(user.getEmail().toLowerCase());
        User optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser != null) {
            throw new UserException("User exist!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void changePassword(String username, String newPassword) {
        User user = findByEmail(username);

        if (newPassword.length() < 12) {
            throw new BadPasswordException("Password length must be 12 chars minimum!");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadPasswordException("The passwords must be different!");
        }

        if (hackPasswords.contains(newPassword)) {
            throw new BadPasswordException("The password is in the hacker's database!");
        }

        userRepository.delete(user);
        user.setPassword(newPassword);
        save(user);
    }
}
