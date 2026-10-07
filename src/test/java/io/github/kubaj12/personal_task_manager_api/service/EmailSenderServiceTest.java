package io.github.kubaj12.personal_task_manager_api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    OtpService otpService;
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    EmailSenderService emailSenderService;

    @Captor
    ArgumentCaptor<SimpleMailMessage> captorMessage;

    String from;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailSenderService, "from", "from@test.com");
    }

    @Test
    void sendEmailVerificationOtp_shouldSendEmailWithGeneratedOtp() {
        when(otpService.generateAndStoreOtp("test@test.com")).thenReturn("hashedOtp");

        emailSenderService.sendEmailVerificationOtp("test@test.com");

        verify(mailSender).send(captorMessage.capture());

        SimpleMailMessage mailMessage = captorMessage.getValue();

        assertThat(mailMessage.getTo()).isEqualTo(new String[] {"test@test.com"});
        assertThat(mailMessage.getSubject()).isEqualTo("Otp password");
        assertThat(mailMessage.getFrom()).isEqualTo("from@test.com");
        assertThat(mailMessage.getText()).isEqualTo("Your one time password: hashedOtp");

        verify(otpService).generateAndStoreOtp("test@test.com");
    }

    @Test
    void sendEmailVerificationOtp_mailSenderCantSentMessage_throwMailSendException() {
        when(otpService.generateAndStoreOtp("test@test.com")).thenReturn("hashedOtp");

        doThrow(new MailSendException("Couldn't send a email")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(MailSendException.class, () -> {
            emailSenderService.sendEmailVerificationOtp("test@test.com");
        });

        verify(otpService).generateAndStoreOtp("test@test.com");
    }
}
