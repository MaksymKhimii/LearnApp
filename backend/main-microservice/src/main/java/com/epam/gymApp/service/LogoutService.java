package com.epam.gymApp.service;

import com.epam.gymApp.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * LogoutService is service with implemented logic of log out process
 *
 * @see TokenRepository
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);

  private final TokenRepository tokenRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
    try {
      logger.info("Logging out user.");
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return;
      }
      jwt = authHeader.substring(7);
      var storedToken = tokenRepository.findByToken(jwt).orElse(null);
      if (storedToken != null) {
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.update(storedToken);
        logger.info("User successfully logged out.");
      }
    } catch (Exception e) {
      logger.error("Error occurred during logout.", e);
    }
  }
}
