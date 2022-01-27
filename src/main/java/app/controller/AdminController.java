package app.controller;

import app.model.RoleRequest;
import app.model.User;
import app.repository.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @PutMapping("/api/admin/user/role")
    public User updateRole(@Valid @RequestBody RoleRequest request) {
        return userService.updateRole(request);
    }

    @GetMapping("/api/admin/user")
    public List<User> getUser() {
        return userService.getAll();
    }

    @Autowired
    private UserRepository userRepo;

    @DeleteMapping(value = {"/api/admin/user", "/api/admin/user/{userEmail}"})
    public Map<String, String> deleteUser(@PathVariable(required = false) String userEmail) {
        userService.deleteUser(userEmail);
        return Map.of("user", userEmail, "status", "Deleted successfully!");
    }
}

