package com.bridgelabz.authservice.controller;

import com.bridgelabz.authservice.dto.AuthRequestDTO;
import com.bridgelabz.authservice.dto.AuthResponseDTO;
import com.bridgelabz.authservice.dto.ResponseDTO;
import com.bridgelabz.authservice.dto.TokenRefreshRequestDTO;
import com.bridgelabz.authservice.dto.TokenRefreshResponseDTO;
import com.bridgelabz.authservice.exception.TokenRefreshException;
import com.bridgelabz.authservice.model.RefreshToken;
import com.bridgelabz.authservice.model.User;
import com.bridgelabz.authservice.repository.UserRepository;
import com.bridgelabz.authservice.security.JwtUtils;
import com.bridgelabz.authservice.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequestDTO signUpRequest) {
        logger.info("Registering user: {}", signUpRequest.getEmail());
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            logger.warn("Email already in use: {}", signUpRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseDTO("Error: Email is already in use!", null));
        }

        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getName(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                "local");

        userRepository.save(user);
        logger.info("User registered successfully: {}", signUpRequest.getEmail());

        return ResponseEntity.ok(new ResponseDTO("User registered successfully!", null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequestDTO loginRequest) {
        logger.info("Authenticating user: {}", loginRequest.getEmail());
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String jwt = jwtUtils.generateJwtToken(user.getEmail());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
                logger.info("User {} logged in successfully. Access and Refresh tokens generated.", loginRequest.getEmail());
                return ResponseEntity.ok(new AuthResponseDTO(jwt, refreshToken.getToken(), user.getEmail(), user.getName()));
            }
        }

        logger.warn("Authentication failed for user: {}", loginRequest.getEmail());
        return ResponseEntity.status(401).body(new ResponseDTO("Error: Invalid credentials", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        logger.info("Received request to refresh token");

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtToken(user.getEmail());
                    RefreshToken rotatedToken = refreshTokenService.createRefreshToken(user.getEmail());
                    logger.info("Successfully refreshed access token for user: {}", user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponseDTO(token, rotatedToken.getToken()));
                })
                .orElseThrow(() -> {
                    logger.warn("Refresh token [{}] is not present in database!", requestRefreshToken);
                    return new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
                });
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        logger.info("Fetching details for user email: {}", email);
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> {
                    logger.warn("User with email {} not found", email);
                    return ResponseEntity.notFound().build();
                });
    }
}