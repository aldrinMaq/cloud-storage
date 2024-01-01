package com.maquiling.cloudstorage.service.user;

import com.maquiling.cloudstorage.model.auth.LoginResponse;
import com.maquiling.cloudstorage.model.auth.User;
import com.maquiling.cloudstorage.model.auth.UserUpdateDTO;
import com.maquiling.cloudstorage.model.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        // Encrypt (hash) the password before saving the user
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);

        return userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword());

    }

    @Transactional
    public LoginResponse authenticateAndGetDetails(String email, String password) {
        boolean isAuthenticated = authenticateUser(email, password);
        if (isAuthenticated) {
            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUsername(user.getUsername());
                loginResponse.setEmail(user.getEmail());
                // Set other needed properties
                return loginResponse;
            }
        }
        return null;
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) throws Exception {
        // Retrieve the existing user (e.g., from the database)
        User user = userRepo.findByUsername(userUpdateDTO.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        // Verify if the provided password matches the user's current password
        if (!passwordEncoder.matches(userUpdateDTO.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password");
        }

        // Update user details
        user.setEmail(userUpdateDTO.getEmail());
        user.setUsername(userUpdateDTO.getUsername());

        userRepo.save(user);
    }

}
