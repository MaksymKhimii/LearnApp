package com.epam.gymApp.service;

import com.epam.gymApp.model.User;
import com.epam.gymApp.model.dto.ChangePasswordDto;
import com.epam.gymApp.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findUserById(long id) {
    return userRepository.findById(id);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public String generateUsername(String firstName, String lastName) {
    StringBuilder username = new StringBuilder();
    Random random = new Random();
    username
        .append(firstName)
        .append("_")
        .append(lastName);
    for (int i = 0; i < 3; i++) {
      int digit = random.nextInt(10);
      username.append(digit);
    }
    if (findUserByUsername(String.valueOf(username)) == null) {
      return String.valueOf(username);
    }
    return generateUsername(firstName, lastName);
  }

  public String generatePassword() {
    int passwordLength = 8;
    String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";

    String allCharacters = uppercaseLetters + lowercaseLetters + digits;
    StringBuilder password = new StringBuilder();

    password.append(digits.charAt(new Random().nextInt(digits.length())));
    password.append(uppercaseLetters.charAt(new Random().nextInt(uppercaseLetters.length())));

    for (int i = 2; i < passwordLength; i++) {
      password.append(allCharacters.charAt(new Random().nextInt(allCharacters.length())));
    }

    return shuffleString(password.toString());
  }

  private String shuffleString(String input) {
    List<Character> characters = new ArrayList<>();
    for (char c : input.toCharArray()) {
      characters.add(c);
    }
    Collections.shuffle(characters);

    StringBuilder shuffled = new StringBuilder();
    for (char c : characters) {
      shuffled.append(c);
    }
    return shuffled.toString();
  }

  public boolean changePassword(ChangePasswordDto changePasswordDto) {
    if ((changePasswordDto == null) || changePasswordDto.getOldPassword()
        .equals(changePasswordDto.getNewPassword())) {
      return false;
    }
    User user = userRepository.findByUsername(changePasswordDto.getUsername());
    logger.info("Encoded new password: "+ changePasswordDto.getNewPassword());
    if (user != null) {
      user.setPassword(changePasswordDto.getNewPassword());
      userRepository.update(user);
    //  userRepository.save(user);
      return true;
    }
    logger.info("user was not found with username: "+changePasswordDto.getUsername());
    return false;
  }

  public void save(User user) {
    userRepository.save(user);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public void update(User user) {
    userRepository.update(user);
  }
}
