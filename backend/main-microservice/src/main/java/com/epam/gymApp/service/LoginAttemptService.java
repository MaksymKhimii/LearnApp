package com.epam.gymApp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LoginAttemptService for Brute Force protection.
 * <p>
 * User will be blocked for 5 minutes on 3 unsuccessful login attempts.
 */
@Service
public class LoginAttemptService {

  private static final Logger logger = Logger.getLogger(LoginAttemptService.class.getName());


  public static final int MAX_ATTEMPT = 3;
  private final LoadingCache<String, Integer> attemptsCache;

  @Autowired
  private HttpServletRequest request;

  public LoginAttemptService() {
    super();
    attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public Integer load(final String key) {
            return 0;
          }
        });
  }

  public void loginFailed(final String key) {
    int attempts;
    try {
      attempts = attemptsCache.get(key);
    } catch (final ExecutionException e) {
      attempts = 0;
    }
    attempts++;
    attemptsCache.put(key, attempts);
    logger.info("attempt number: " + attempts);
  }

  public boolean isBlocked() {
    try {
      if (attemptsCache.get(getClientIP()) >= MAX_ATTEMPT) {
        logger.warning("Authentication was failed. User was blocked!");
      }
      return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
    } catch (final ExecutionException e) {
      logger.warning("Authentication was failed. User was blocked!");
      return false;
    }
  }

  private String getClientIP() {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader != null) {
      return xfHeader.split(",")[0];
    }
    return request.getRemoteAddr();
  }
}
