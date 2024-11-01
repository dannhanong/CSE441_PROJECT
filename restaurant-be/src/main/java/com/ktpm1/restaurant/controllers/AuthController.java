package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.ChangePasswordForm;
import com.ktpm1.restaurant.dtos.request.LoginForm;
import com.ktpm1.restaurant.dtos.request.SignupForm;
import com.ktpm1.restaurant.dtos.request.UpdateProfile;
import com.ktpm1.restaurant.dtos.response.LoginResponse;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.RoleName;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.services.EmailService;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.RoleService;
import com.ktpm1.restaurant.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@RequestBody SignupForm signupForm) {
        if(userService.existsByUsername(signupForm.getUsername())){
            return new ResponseEntity<>(ResponseMessage.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("username_exists")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail(signupForm.getEmail())){
            return new ResponseEntity<>(ResponseMessage.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("email_exists")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        if (!signupForm.getPassword().equals(signupForm.getRePassword())) {
            return new ResponseEntity<>(ResponseMessage.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("password_not_match")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        User user = new User(signupForm.getName(), signupForm.getUsername(),
                passwordEncoder.encode(signupForm.getPassword()),
                signupForm.getEmail(), signupForm.getPhoneNumber());
        Set<String> strRoles = signupForm.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null){
            Role userRole = roleService.findByName(RoleName.USER);
            roles.add(userRole);
        }else {
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleService.findByName(RoleName.ADMIN);
                        Role adminUserRole = roleService.findByName(RoleName.USER);
                        roles.add(adminRole);
                        roles.add(adminUserRole);
                        break;
                    case "user":
                        Role userRole = roleService.findByName(RoleName.USER);
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);

        try {
            User savedUser = userService.save(user);

            if (savedUser == null) {
                return new ResponseEntity<>(ResponseMessage.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("createFailed")
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

//            emailService.sendVerificationEmail(savedUser);
//            return new ResponseEntity<>(ResponseMessage.builder()
//                    .status(HttpStatus.CREATED.value())
//                    .message("createSuccess")
//                    .build(), HttpStatus.CREATED);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResponseMessage.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("createFailed")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));

            if (!userService.isEnableUser(loginForm.getUsername())) {
                return new ResponseEntity<>(ResponseMessage.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("user_disabled")
                        .build(), HttpStatus.UNAUTHORIZED);
            }

            if (authentication.isAuthenticated()) {
                final String accessToken = jwtService.generateToken(loginForm.getUsername());

                LoginResponse tokens = LoginResponse.builder()
                        .accessToken(accessToken)
                        .build();

                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(ResponseMessage.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("login_failed")
                    .build(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("login_failed")
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        jwtService.deleteToken(token);
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("logout_success")
                .build(), HttpStatus.OK);
    }

    @PutMapping("/update-verify-code")
    public ResponseEntity<ResponseMessage> updateVerificationCode(@RequestParam("username") String username,
                                                    @RequestParam("code") String code) {
        userService.updateVerificationCode(username, code);
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("update_success")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<ResponseMessage> verifyUser(@RequestParam("code") String code){
        boolean verified = userService.verify(code);
        String message = verified ? "Your account has been verified. You can now login." : "Verification failed. Please contact the administrator.";
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(verified ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build(), verified ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/profile")
    public ResponseEntity<User> getProfile(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<ResponseMessage> updateProfile(@ModelAttribute UpdateProfile updateProfile,
                                                         HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return new ResponseEntity<>(userService.updateProfile(updateProfile, username), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordForm changePasswordForm,
                                                          HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return new ResponseEntity<>(userService.changePassword(username, changePasswordForm), HttpStatus.OK);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
