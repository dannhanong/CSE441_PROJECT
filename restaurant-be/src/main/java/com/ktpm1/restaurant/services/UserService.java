package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.ChangePasswordForm;
import com.ktpm1.restaurant.dtos.request.UpdateProfile;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
    Page<User> getAllUser(Pageable pageable);
    Page<User> searchUserByKeyword(String keyword, Pageable pageable);
    ResponseMessage changePassword(String username, ChangePasswordForm changePasswordForm);
    User updateUser(User user, String username);
    boolean isEnableUser(String username);
    boolean verify(String verificationCode);
    ResponseMessage updateProfile(UpdateProfile updateProfile, String username);
}
