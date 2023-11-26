package com.epam.gymApp.listener;

import com.epam.gymApp.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * AuthenticationFailureListener is listener for brute force protection. It can track failed
 * authorization attempts.
 *
 * @see AuthenticationFailureBadCredentialsEvent
 * @see LoginAttemptService
 */
@Component
public class AuthenticationFailureListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
      loginAttemptService.loginFailed(request.getRemoteAddr());
    } else {
      loginAttemptService.loginFailed(xfHeader.split(",")[0]);
    }
  }
}