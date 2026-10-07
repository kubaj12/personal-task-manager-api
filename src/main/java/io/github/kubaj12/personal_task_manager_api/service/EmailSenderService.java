package io.github.kubaj12.personal_task_manager_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailSenderService {
    final OtpService otpService;
    final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Async("emailThreadPoolTaskExecutor")
    public void sendEmailVerificationOtp(String email) {
        String otp = otpService.generateAndStoreOtp(email);
        String message = "Your one time password: " + otp;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Otp password");
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setText(message);

        mailSender.send(simpleMailMessage);
    }
}
