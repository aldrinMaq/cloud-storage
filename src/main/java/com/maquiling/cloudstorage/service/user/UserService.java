package com.maquiling.cloudstorage.service.user;

import com.maquiling.cloudstorage.model.auth.User;
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

}
