package com.epam.gymApp.service;

import com.epam.gymApp.model.Token;
import com.epam.gymApp.model.TokenType;
import com.epam.gymApp.model.User;
import com.epam.gymApp.model.dto.AuthenticationRequest;
import com.epam.gymApp.model.dto.AuthenticationResponse;
import com.epam.gymApp.model.dto.RegisterRequest;
import com.epam.gymApp.repository.TokenRepository;
import com.epam.gymApp.repository.UserRepository;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstName(request.getFirstname())
        .lastName(request.getLastname())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .isActive(true)
        .build();
    if (userRepository.findByUsername(user.getUsername()) != null) {
      return null;
    }
    var savedUser = userRepository.saveNewUser(user);
    var jwtToken = jwtService.generateToken(user);

    saveUserToken(savedUser, jwtToken);
    logger.info("A new token was generated: " + jwtToken);
    return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();
  }

  public String register(User user) {
    if (userRepository.findByUsername(user.getUsername()) != null) {
      return null;
    }
    var savedUser = userRepository.saveNewUser(user);
    var jwtToken = jwtService.generateToken(user);

    saveUserToken(savedUser, jwtToken);
    logger.info("A new token was generated: " + jwtToken);
    return jwtToken;
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    var user = userRepository.findByUsername(request.getUsername());
    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    logger.info("A new token was generated: " + jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUsername());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(t -> {
      t.setExpired(true);
      t.setRevoked(true);
    });
    logger.info("All user's tokens were revoked and expired");
    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .revoked(false)
        .expired(false)
        .build();
    tokenRepository.save(token);
  }
}
