package com.sentrafarma.auth.service.impl;

import com.sentrafarma.auth.entity.PasswordResetToken;
import com.sentrafarma.auth.entity.Role;
import com.sentrafarma.auth.entity.User;
import com.sentrafarma.auth.payload.*;
import com.sentrafarma.auth.repository.PasswordResetTokenRepository;
import com.sentrafarma.auth.repository.UserRepository;
import com.sentrafarma.auth.security.JwtUtil;
import com.sentrafarma.auth.service.AuthService;
import com.sentrafarma.auth.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordResetTokenRepository tokenRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Konfirmasi password tidak cocok dengan password");
        }

        if (request.getNik() != null && !request.getNik().trim().isEmpty()) {
            String nik = request.getNik().trim();
            if (!nik.matches("^(?!(\\d)\\1{15}$)\\d{16}$")) {
                throw new IllegalArgumentException("NIK harus 16 digit angka dan tidak boleh berupa angka berulang sembarangan (contoh: 3333333333333333)");
            }
        }

        if (request.getNoTelepon() != null && !request.getNoTelepon().trim().isEmpty()) {
            String phone = request.getNoTelepon().trim();
            if (!phone.matches("^(08|628)[0-9]{8,11}$")) {
                throw new IllegalArgumentException("Nomor telepon tidak valid. Harus diawali 08 atau 628 dengan 10-13 digit angka");
            }
        }

        if (request.getTanggalLahir() != null && !request.getTanggalLahir().trim().isEmpty()) {
            try {
                java.time.LocalDate dob = java.time.LocalDate.parse(request.getTanggalLahir().trim());
                if (dob.isAfter(java.time.LocalDate.now())) {
                    throw new IllegalArgumentException("Tanggal lahir pasien (" + request.getTanggalLahir() + ") tidak boleh di masa depan. Silakan pilih tanggal hari ini atau sebelumnya.");
                }
            } catch (java.time.format.DateTimeParseException e) {
                throw new IllegalArgumentException("Format tanggal lahir tidak valid (Gunakan YYYY-MM-DD)");
            }
        }

        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new IllegalArgumentException("Email sudah terdaftar dalam sistem");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .namaLengkap(request.getNamaLengkap())
                .role(request.getRole() != null ? request.getRole() : Role.PASIEN)
                .noTelepon(request.getNoTelepon())
                .isDeleted(false)
                .build();

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(mapToDto(user))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email atau password tidak ditemukan"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email atau password tidak sesuai");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(mapToDto(user))
                .build();
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email tidak terdaftar"));

        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken tokenEntity = PasswordResetToken.builder()
                .token(resetToken)
                .email(user.getEmail())
                .expiryDate(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        tokenRepository.save(tokenEntity);

        emailService.sendResetPasswordEmail(user.getEmail(), user.getNamaLengkap(), resetToken);

        return resetToken;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("Konfirmasi password baru tidak cocok");
        }

        PasswordResetToken resetToken = tokenRepository.findByTokenAndUsedFalse(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token reset password tidak valid atau sudah digunakan"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token reset password sudah kedaluwarsa");
        }

        User user = userRepository.findByEmailAndIsDeletedFalse(resetToken.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    @Override
    public Page<UserDto> getUsers(int page, int limit, String search, Role role) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").descending());

        Page<User> users;
        if (search != null && !search.isBlank()) {
            users = userRepository.findByNamaLengkapContainingIgnoreCaseAndIsDeletedFalse(search, pageable);
        } else if (role != null) {
            users = userRepository.findByRoleAndIsDeletedFalse(role, pageable);
        } else {
            users = userRepository.findByIsDeletedFalse(pageable);
        }

        return users.map(this::mapToDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.getIsDeleted())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan dengan ID: " + id));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .filter(u -> !u.getIsDeleted())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        if (dto.getNamaLengkap() != null) user.setNamaLengkap(dto.getNamaLengkap());
        if (dto.getNoTelepon() != null) user.setNoTelepon(dto.getNoTelepon());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        return mapToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .namaLengkap(user.getNamaLengkap())
                .role(user.getRole())
                .noTelepon(user.getNoTelepon())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
