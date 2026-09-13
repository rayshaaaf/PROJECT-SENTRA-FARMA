package com.sentrafarma.auth.controller;

import com.sentrafarma.auth.entity.Role;
import com.sentrafarma.auth.payload.*;
import com.sentrafarma.auth.security.JwtUtil;
import com.sentrafarma.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Users", description = "Endpoint Otentikasi dan Manajemen User Sentra Farma")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Pendaftaran User/Pasien Baru")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Pengguna Multi-Role")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Pengajuan Lupa Password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = authService.forgotPassword(request);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Token reset password telah dikirimkan ke email Anda. Silakan periksa Kotak Masuk / Inbox email Anda."
        ));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password Pengguna")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password berhasil diperbarui. Silakan login kembali."));
    }

    @GetMapping("/me")
    @Operation(summary = "Get Profile / Verifikasi Session JWT")
    public ResponseEntity<UserDto> getMe(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header Authorization tidak valid");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token kedaluwarsa atau tidak valid");
        }
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(authService.getUserById(userId));
    }

    @GetMapping("/users")
    @Operation(summary = "Get List Users dengan Search, Filter Role, & Pagination")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(authService.getUsers(page, limit, search, role));
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get User Detail by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update Data User")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(authService.updateUser(id, dto));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Soft Delete User")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User berhasil dihapus (soft delete)."));
    }
}
