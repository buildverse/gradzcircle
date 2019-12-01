package com.drishika.gradzcircle.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.UserRepository;
import com.drishika.gradzcircle.security.SecurityUtils;
import com.drishika.gradzcircle.service.MailService;
import com.drishika.gradzcircle.service.UserService;
import com.drishika.gradzcircle.service.dto.UserDTO;
import com.drishika.gradzcircle.web.rest.vm.KeyAndPasswordVM;
import com.drishika.gradzcircle.web.rest.vm.ManagedUserVM;
import com.drishika.gradzcircle.web.rest.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.drishika.gradzcircle.service.dto.PasswordChangeDTO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	private final MailService mailService;

	private static final String CHECK_ERROR_MESSAGE = "Incorrect password";

	public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

		this.userRepository = userRepository;
		this.userService = userService;
		this.mailService = mailService;
	}

	
	/**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @throws InvalidPasswordException 400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     */
    @PostMapping("/register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {throw new EmailAlreadyUsedException();});
        User user = userService.registerUser(managedUserVM.getLogin(), managedUserVM.getPassword(),
				managedUserVM.getFirstName(), managedUserVM.getLastName(),
				managedUserVM.getEmail().toLowerCase(), managedUserVM.getImageUrl(),
				managedUserVM.getLangKey(), managedUserVM.getAuthorities(),
				managedUserVM.getCompanyName(), managedUserVM.getPhoneNumber(),
				managedUserVM.getCountryLocation());
        mailService.sendActivationEmail(user);
    }


	/**
	 * GET /activate : activate the registered user.
	 *
	 * @param key
	 *            the activation key
	 * @return the ResponseEntity with status 200 (OK) and the activated user in
	 *         body, or status 500 (Internal Server Error) if the user couldn't be
	 *         activated
	 */
	@GetMapping("/activate")
	@Timed
	 public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }
	/*public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
		return userService.activateRegistration(key).map(user -> new ResponseEntity<String>(HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
*/
	/**
	 * GET /authenticate : check if the user is authenticated, and return its login.
	 *
	 * @param request
	 *            the HTTP request
	 * @return the login if the user is authenticated
	 */
	@GetMapping("/authenticate")
	@Timed
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * GET /account : get the current user.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the current user in body,
	 *         or status 500 (Internal Server Error) if the user couldn't be
	 *         returned
	 */
	@GetMapping("/account")
	@Timed
	public UserDTO getAccount() {
		 return userService.getUserWithAuthorities()
		            .map(UserDTO::new)
		            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
	}

	/**
	 * POST /account : update the current user information.
	 *
	 * @param userDTO
	 *            the current user information
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request)
	 *         or 500 (Internal Server Error) if the user couldn't be updated
	 */
	@PostMapping("/account")
	@Timed
	public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
   }
	
	
	/**
	 * POST /account/change-password : changes the current user's password
	 *
	 * @param password
	 *            the new password
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request)
	 *         if the new password is not strong enough
	 */
	@PostMapping(path = "/account/change-password")
	@Timed
	 public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
   }

	/**
	 * POST /account/reset-password/init : Send an email to reset the password of
	 * the user
	 *
	 * @param mail
	 *            the mail of the user
	 * @return the ResponseEntity with status 200 (OK) if the email was sent, or
	 *         status 400 (Bad Request) if the email address is not registered
	 */
	@PostMapping(path = "/account/reset-password/init")
	@Timed
	 public void requestPasswordReset(@RequestBody String mail) {
	       mailService.sendPasswordResetMail(
	           userService.requestPasswordReset(mail)
	               .orElseThrow(EmailNotFoundException::new)
	       );
	    }


	/**
	 * POST /account/reset-password/finish : Finish to reset the password of the
	 * user
	 *
	 * @param keyAndPassword
	 *            the generated key and the new password
	 * @return the ResponseEntity with status 200 (OK) if the password has been
	 *         reset, or status 400 (Bad Request) or 500 (Internal Server Error) if
	 *         the password could not be reset
	 */
	@PostMapping(path = "/account/reset-password/finish")
	@Timed
	public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }


	private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
