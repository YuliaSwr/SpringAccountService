package app.service;

import app.exception.*;
import app.model.RoleRequest;
import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordChecker passwordChecker;

    private final Set<String> BUSSINESS_ROLES = Set.of("ROLE_ACCOUNTANT", "ROLE_USER");

    private static final String OP_REMOVE = "REMOVE";
    private static final String OP_GRANT = "GRANT";

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserException::new);
    }

    public User save(User user) {

        passwordChecker.check(user.getPassword());
        user.setEmail(user.getEmail().toLowerCase());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("User exist!");
        }

        user.setPassword(passwordChecker.encode(user.getPassword()));

        if (userRepository.findAll().isEmpty()) {
            user.setRoles(Set.of("ROLE_ADMINISTRATOR"));
        } else {
            user.setRoles(Set.of("ROLE_USER"));
        }

        userRepository.save(user);
        return user;
    }

    public void changePassword(String username, String newPassword) {
        User user = findByEmail(username);

        passwordChecker.check(newPassword);
        passwordChecker.checkNewPassword(user.getPassword(), newPassword);

        user.setPassword(passwordChecker.encode(newPassword));
        userRepository.save(user);
    }

    private User putRole(User user, String newRole) {
        if (roleIsNotExist(newRole)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        if (user.getRoles().contains("ROLE_ADMINISTRATOR") && (BUSSINESS_ROLES.contains(newRole))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }

        if (newRole.equals("ROLE_ADMINISTRATOR") &&
                (user.getRoles().contains("ROLE_ACCOUNTANT") || user.getRoles().contains("ROLE_USER"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }

        user.getRoles().add(newRole);
        userRepository.save(user);
        return user;
    }

    private boolean roleIsNotExist(String role) {
        return !List.of("ROLE_ADMINISTRATOR", "ROLE_ACCOUNTANT", "ROLE_USER").contains(role);
    }

    public User updateRole(RoleRequest request) {
        User user = checkIfUserExists(request.getUser());

        if (request.getOperation().equals(OP_REMOVE)) {
            return deleteRole(user, "ROLE_" + request.getRole());
        } else if (request.getOperation().equals(OP_GRANT)) {
            return putRole(user, "ROLE_" + request.getRole());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unrecognised operation");
        }
    }

    private User checkIfUserExists(String email) {
        return userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    private User deleteRole(User user, String delRole) {
        if (roleIsNotExist(delRole)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        if (!user.getRoles().contains(delRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }

        if (delRole.equals("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        if (user.getRoles().size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }

        user.getRoles().remove(delRole);
        return userRepository.save(user);
    }

    public void deleteUser(String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty for DELETE request!");
        }

        User user = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (user.getRoles().contains("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(user);
    }

    public List<User> getAll() {
        return userRepository.findAllByOrderByIdAsc();
    }
}
