package org.example.serviceuser.userService.mapper;

import org.example.serviceuser.userService.dto.RegisterCreationRequest;
import org.example.serviceuser.userService.entity.User;
import org.example.serviceuser.userService.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUser(RegisterCreationRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        // Xác định role từ request, mặc định là CUSTOMER
        UserRole role = UserRole.CUSTOMER;
        try {
            role = UserRole.valueOf(registerRequest.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Log lỗi nếu cần thiết hoặc giữ mặc định là CUSTOMER
        }
        user.setRole(role);
        return user;
    }
}
