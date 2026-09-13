package com.sentrafarma.auth.service;

public interface EmailService {
    void sendResetPasswordEmail(String toEmail, String namaLengkap, String resetToken);
}
