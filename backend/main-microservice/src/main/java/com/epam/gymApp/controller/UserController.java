package com.epam.gymApp.controller;

import com.epam.gymApp.model.dto.ChangePasswordDto;
import com.epam.gymApp.model.dto.UserDto;
import com.epam.gymApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user-related actions.
 * <p>
 * This controller handles user change operation.
 *
 * @see UserDto
 * @see ChangePasswordDto
 * @see UserService
 */
@RestController
@RequestMapping("/gym-app/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

  /**
   * Change user's password.
   * <p>
   * This method allows a user to change their password based on the provided ChangePasswordDto. If
   * the password change is successful, an OK status (HttpStatus.OK) is returned. If the password
   * change fails, a BAD_REQUEST status (HttpStatus.BAD_REQUEST) is returned.
   *
   * @param changePasswordDto Data transfer object containing the old and new password.
   * @return ResponseEntity with HttpStatus OK on successful password change, or BAD_REQUEST on
   * failure.
   */
  @PutMapping("/password/update")
  public ResponseEntity<HttpStatus> changePassword(
      @RequestBody ChangePasswordDto changePasswordDto) {
    logger.info("change password controller =>");
    String encodedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
    changePasswordDto.setNewPassword(encodedPassword);
    boolean isPasswordWasChanged = userService.changePassword(changePasswordDto);
    logger.info("password was changed to: "+ changePasswordDto.getNewPassword());
    if (isPasswordWasChanged) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
