package com.sentrafarma.auth.service.impl;

import com.sentrafarma.auth.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String toEmail, String namaLengkap, String resetToken) {
        log.info("------------------------------------------------------------------");
        log.info("✉️ [EMAIL RESET PASSWORD] MEMPROSES PENGIRIMAN TOKEN");
        log.info("Tujuan Email : {}", toEmail);
        log.info("Nama Pasien  : {}", namaLengkap);
        log.info("Token Reset  : {}", resetToken);
        log.info("------------------------------------------------------------------");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("fandanirais6@gmail.com", "Sentra Farma Care");
            helper.setTo(toEmail);
            helper.setSubject("[Sentra Farma] Kode Token Reset Password Anda");
            
            String htmlContent = buildEmailTemplate(namaLengkap != null ? namaLengkap : "Pengguna", resetToken);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("✅ Email reset password berhasil terkirim ke: {}", toEmail);
        } catch (Exception e) {
            log.warn("⚠️ Notifikasi SMTP Email: Pengiriman email eksternal offline/dummy (Token tetap aktif untuk reset password). Detail: {}", e.getMessage());
        }
    }

    private String buildEmailTemplate(String nama, String token) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <style>
                    body { font-family: 'Plus Jakarta Sans', Arial, sans-serif; background-color: #F8FAFC; color: #1E293B; margin: 0; padding: 20px; }
                    .container { max-width: 520px; margin: 0 auto; background: #ffffff; padding: 32px; border-radius: 24px; border: 1px solid #E2E8F0; box-shadow: 0 10px 25px -5px rgba(16, 185, 129, 0.1); }
                    .header { text-align: center; border-bottom: 1px solid #F1F5F9; padding-bottom: 20px; margin-bottom: 24px; }
                    .title { font-size: 20px; font-weight: 800; color: #064E3B; margin-top: 10px; }
                    .badge { background: #ECFDF5; color: #047857; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-block; }
                    .token-box { background: #F0FDF4; border: 2px dashed #34D399; padding: 16px; border-radius: 16px; text-align: center; font-family: monospace; font-size: 18px; font-weight: 800; color: #065F46; letter-spacing: 1px; margin: 20px 0; word-break: break-all; }
                    .footer { text-align: center; font-size: 11px; color: #94A3B8; margin-top: 30px; border-top: 1px solid #F1F5F9; padding-top: 16px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <span class="badge">Sentra Farma Verification</span>
                        <div class="title">Reset Password Akun Sentra Farma</div>
                    </div>
                    <p>Halo <strong>""" + nama + """
                    </strong>,</p>
                    <p>Kami menerima permintaan untuk melakukan reset password akun Sentra Farma Anda. Gunakan kode token di bawah ini untuk memperbarui password Anda:</p>
                    
                    <div class="token-box">""" + token + """
                    </div>
                    
                    <p style="font-size: 12px; color: #64748B;">
                        ⏰ Token ini berlaku selama <strong>1 Jam</strong>. Apabila Anda tidak merasa melakukan permintaan ini, silakan abaikan pesan email ini.
                    </p>

                    <div class="footer">
                        Sentra Farma Integrated Medical & Pharmacy Center<br>
                        Email ini dikirim secara otomatis oleh Sistem Sentra Farma Care.
                    </div>
                </div>
            </body>
            </html>
        """;
    }
}
