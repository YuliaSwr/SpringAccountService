package app.service;

import app.exception.BadPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class PasswordChecker {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private ArrayList<String> hackPasswords = new ArrayList<String>(Arrays.asList("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));


    public void check(String password) {
        if (password.length() < 12) {
            throw new BadPasswordException("Password length must be 12 chars minimum!");
        }

        if (hackPasswords.contains(password)) {
            throw new BadPasswordException("The password is in the hacker's database!");
        }
    }

    public void checkNewPassword(String exPassword, String newPassword) {
        if (passwordEncoder.matches(newPassword, exPassword)) {
            throw new BadPasswordException("The passwords must be different!");
        }
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
