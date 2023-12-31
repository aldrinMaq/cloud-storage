package com.maquiling.cloudstorage.rest.user;

import com.maquiling.cloudstorage.model.auth.LoginRequest;
import com.maquiling.cloudstorage.model.auth.User;
import com.maquiling.cloudstorage.model.repo.UserRepo;
import com.maquiling.cloudstorage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // Allow CORS for all methods in this controller
public class UserRest {

    private UserRepo userRepo;
    private UserService userService;
    @Autowired
    public UserRest(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            boolean isAuthenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (isAuthenticated) {
                return ResponseEntity.ok("User authenticated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Login error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
