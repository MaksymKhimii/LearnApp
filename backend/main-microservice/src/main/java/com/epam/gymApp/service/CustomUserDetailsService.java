package com.epam.gymApp.service;

import com.epam.gymApp.model.User;
import com.epam.gymApp.repository.UserRepository;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomUserDetailsService for spring security implementation.
 *
 * @see UserRepository
 * @see LoginAttemptService
 * @see UserDetailsService
 */
@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (loginAttemptService.isBlocked()) {
      throw new RuntimeException("blocked");
    }

    try {
      User user = userRepository.findByUsername(username);
      if (user == null) {
        return new org.springframework.security.core.userdetails.User(
            " ",
            " ",
            true,
            true,
            true,
            true,
            new HashSet<>());
      }

      return new org.springframework.security.core.userdetails.User(
          user.getUsername(),
          user.getPassword(),
          user.isEnabled(),
          true,
          true,
          true,
          new HashSet<>());
    } catch (UsernameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
