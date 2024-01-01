package com.maquiling.cloudstorage.rest.user;

import com.cloudinary.api.ApiResponse;
import com.maquiling.cloudstorage.model.auth.LoginRequest;
import com.maquiling.cloudstorage.model.auth.LoginResponse;
import com.maquiling.cloudstorage.model.auth.UserUpdateDTO;
import com.maquiling.cloudstorage.model.auth.User;
import com.maquiling.cloudstorage.model.repo.UserRepo;
import com.maquiling.cloudstorage.service.CloudinaryService;
import com.maquiling.cloudstorage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // Allow CORS for all methods in this controller
public class UserRest {

    private final UserRepo userRepo;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public UserRest(UserRepo userRepo, UserService userService, CloudinaryService cloudinaryService) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (userRepo.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest().body("Username already exists, account cannot be created");
            }
            if (userRepo.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists, account cannot be created");
            }
            /// Create folder with the username as the folder path
            ApiResponse folderCreationResponse = cloudinaryService.createFolder(user.getUsername());
            // Check for a successful response
            if (folderCreationResponse == null || !isFolderCreationSuccessful(folderCreationResponse)) {
                return ResponseEntity.badRequest().body("Unable to create user folder");
            }

            // Create user if folder creation is successful
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
            LoginResponse loginResponse = userService.authenticateAndGetDetails(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            if (loginResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Login error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            userService.updateUser(userUpdateDTO);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Update error: " + e.getMessage());
        }
    }

    private boolean isFolderCreationSuccessful(ApiResponse response) {
        return response.containsKey("success") && (Boolean) response.get("success");
    }

}
