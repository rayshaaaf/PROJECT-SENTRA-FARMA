package com.sentrafarma.auth.service;

import com.sentrafarma.auth.entity.Role;
import com.sentrafarma.auth.payload.*;
import org.springframework.data.domain.Page;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    String forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    Page<UserDto> getUsers(int page, int limit, String search, Role role);
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto dto);
    void deleteUser(Long id);
}
