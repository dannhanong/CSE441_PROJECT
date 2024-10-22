package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.ChangePasswordForm;
import com.ktpm1.restaurant.dtos.request.UpdateProfile;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.FileUpload;
import com.ktpm1.restaurant.models.Role;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.EmailService;
import com.ktpm1.restaurant.services.FileUploadService;
import com.ktpm1.restaurant.services.RoleService;
import com.ktpm1.restaurant.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByEmail(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        user.setEnabled(false);
        user.setVerificationCode(generateVerificationCode());
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUserByKeyword(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public ResponseMessage changePassword(String username, ChangePasswordForm changePasswordForm) {
        User currentUser = userRepository.findByUsername(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (currentUser == null) {
            return ResponseMessage.builder()
                    .status(404)
                    .message("Người dùng không tồn tại")
                    .build();
        }

        if (passwordEncoder.matches(changePasswordForm.getOldPassword(), currentUser.getPassword())) {
            if (!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {
                return ResponseMessage.builder()
                        .status(400)
                        .message("Mật khẩu mới không khớp")
                        .build();
            }

            currentUser.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
            userRepository.save(currentUser);
            return ResponseMessage.builder()
                    .status(200)
                    .message("Đổi mật khẩu mới thành công")
                    .build();
        } else {
            return ResponseMessage.builder()
                    .status(400)
                    .message("Mật khẩu cũ không đúng")
                    .build();
        }
    }

    @Override
    public User updateUser(User user, String username) {
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return null;
        }
        currentUser.setName(user.getName());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(currentUser);
    }

    @Override
    public boolean isEnableUser(String username) {
        User user = userRepository.findByUsername(username);
        return user.isEnabled();
    }

    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        }else {
            enableUser(user.getId());
            return true;
        }
    }

    @Override
    public ResponseMessage updateProfile(UpdateProfile updateProfile, String username) {
        User currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            return ResponseMessage.builder()
                    .status(404)
                    .message("user_not_found")
                    .build();
        }
        currentUser.setName(updateProfile.getName());
        currentUser.setPhoneNumber(updateProfile.getPhone());

        MultipartFile avatar = updateProfile.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            try {
                String avatarCode = currentUser.getAvatarCode();
                FileUpload fileUpload = fileUploadService.uploadFile(avatar);
                currentUser.setAvatarCode(fileUpload.getFileCode());
                fileUploadService.deleteFileByFileCode(avatarCode);
            } catch (Exception e) {
                return ResponseMessage.builder()
                        .status(500)
                        .message("Lỗi khi cập nhật ảnh đại diện")
                        .build();
            }
        }

        userRepository.save(currentUser);
        return ResponseMessage.builder()
                .status(200)
                .message("Cập nhật thông tin thành công")
                .build();
    }

    @Override
    public void updateVerificationCode(String username, String verificationCode) {
        User user = userRepository.findByUsername(username);
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
    }

    private void enableUser(Long id) {
        User user = userRepository.findById(id).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        org.springframework.security.core.userdetails.User us = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), rolesToAuthorities(user.getRoles()));
        return us;
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role ->new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }

    private String generateVerificationCode() {
//        return UUID.randomUUID().toString();
        return RandomStringUtils.randomNumeric(6);
    }
}
