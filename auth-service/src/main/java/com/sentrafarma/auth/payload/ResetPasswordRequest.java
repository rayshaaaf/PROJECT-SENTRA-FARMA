package com.sentrafarma.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    @NotBlank(message = "Token wajib diisi")
    private String token;

    @NotBlank(message = "Password baru wajib diisi")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String newPassword;

    @NotBlank(message = "Konfirmasi password wajib diisi")
    private String newPasswordConfirmation;

    public ResetPasswordRequest() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getNewPasswordConfirmation() { return newPasswordConfirmation; }
    public void setNewPasswordConfirmation(String newPasswordConfirmation) { this.newPasswordConfirmation = newPasswordConfirmation; }
}
