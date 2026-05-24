package com.bridgelabz.authservice.controller;

import com.bridgelabz.authservice.dto.AuthRequestDTO;
import com.bridgelabz.authservice.dto.AuthResponseDTO;
import com.bridgelabz.authservice.dto.ResponseDTO;
import com.bridgelabz.authservice.model.User;
import com.bridgelabz.authservice.repository.UserRepository;
import com.bridgelabz.authservice.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDTO signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Error: Email is already in use!", null));
        }

        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getName(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                "local");

        userRepository.save(user);

        return ResponseEntity.ok(new ResponseDTO("User registered successfully!", null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String jwt = jwtUtils.generateJwtToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponseDTO(jwt, user.getEmail(), user.getName()));
            }
        }

        return ResponseEntity.status(401).body(new ResponseDTO("Error: Invalid credentials", null));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
