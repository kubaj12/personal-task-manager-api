package io.github.kubaj12.personal_task_manager_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.eq;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.kubaj12.personal_task_manager_api.config.OtpConfigProperties;
import io.github.kubaj12.personal_task_manager_api.exception.OtpLockAcquisitionException;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {
    @Mock
    RedissonClient redissonClient;

    @Mock
    RBucket<String> rbucket;

    @Mock
    RLock rlock;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    OtpConfigProperties otpConfigProperties;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    OtpService otpService;

    @Test
    void generateAndStoreOtpTest() throws InterruptedException {

        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(true);
        when(otpConfigProperties.length()).thenReturn(10);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_otp");
        when(redissonClient.<String>getBucket("otp:test@test.com")).thenReturn(rbucket);
        when(otpConfigProperties.ttl()).thenReturn(Duration.ofMinutes(10));
        when(rlock.isHeldByCurrentThread()).thenReturn(true);

        String otp = otpService.generateAndStoreOtp("test@test.com");

        assertEquals(otp.length(), 10);

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(otpConfigProperties).ttl();
        verify(passwordEncoder).encode(anyString());
        verify(redissonClient).getBucket("otp:test@test.com");
        verify(rbucket).set(anyString(), eq(Duration.ofMinutes(10)));
    }

    @Test
    void shouldNotGenerateAndStoreOtpWhenLengthEqualToZero() throws InterruptedException {
        String email = "test@test.com";

        when(redissonClient.getLock("lock:otp:" + email)).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(true);
        when(otpConfigProperties.length()).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> {
            otpService.generateAndStoreOtp(email);
        });

        verify(redissonClient).getLock("lock:otp:" + email);
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(otpConfigProperties).length();
        verify(passwordEncoder, never()).encode(anyString());
        verify(redissonClient, never()).<String>getBucket("otp:" + email);
        verify(rbucket, never()).set("hashed_otp", Duration.ofMinutes(10));
        verify(otpConfigProperties, never()).ttl();
    }

    @Test
    void shouldValidateAndConsumeOtpWhenOtpIsValidAndLockIsAcquired() throws InterruptedException {
        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(true);
        when(redissonClient.<String>getBucket("otp:test@test.com")).thenReturn(rbucket);
        when(rbucket.get()).thenReturn("hashedOtp");
        when(passwordEncoder.matches("otp", "hashedOtp")).thenReturn(true);
        when(rlock.isHeldByCurrentThread()).thenReturn(true);

        boolean result = otpService.validateAndConsumeOtp("test@test.com", "otp");

        assertTrue(result);

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(redissonClient).<String>getBucket("otp:test@test.com");
        verify(rbucket).get();
        verify(passwordEncoder).matches("otp", "hashedOtp");
        verify(rbucket).delete();
        verify(rlock).unlock();
    }

    @Test
    void shouldNotValidateAndConsumeOtpWhenOtpIsNotValidAndLockIsAcquired() throws InterruptedException {
        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(true);
        when(redissonClient.<String>getBucket("otp:test@test.com")).thenReturn(rbucket);
        when(rbucket.get()).thenReturn("hashedOtp");
        when(passwordEncoder.matches("notValidOtp", "hashedOtp")).thenReturn(false);
        when(rlock.isHeldByCurrentThread()).thenReturn(true);

        boolean result = otpService.validateAndConsumeOtp("test@test.com", "notValidOtp");

        assertFalse(result);

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(redissonClient).<String>getBucket("otp:test@test.com");
        verify(rbucket).get();
        verify(passwordEncoder).matches("notValidOtp", "hashedOtp");
        verify(rbucket, never()).delete();
        verify(rlock).isHeldByCurrentThread();
        verify(rlock).unlock();
    }

    @Test
    void shouldNotValidateAndConsumeOtpWhenLockIsNotAcquired() throws InterruptedException {
        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(false);

        OtpLockAcquisitionException exception = assertThrows(OtpLockAcquisitionException.class, () ->
        {
            otpService.validateAndConsumeOtp("test@test.com", "otp");
        });

        assertTrue(exception.getMessage().contains("Failed to acquire lock for email test@test.com within the timeout period."));

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(redissonClient, never()).<String>getBucket(anyString());
        verify(rbucket, never()).get();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(rbucket, never()).delete();
        verify(rlock, never()).unlock();
    }

    @Test
    void shouldNotValidateAndConsumeOtpWhenIsInterruptedException() throws InterruptedException {
        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenThrow(new InterruptedException());

        OtpLockAcquisitionException exception = assertThrows(OtpLockAcquisitionException.class, () -> { otpService.validateAndConsumeOtp("test@test.com", "otp");});

        assertTrue(exception.getMessage().contains("Thread was interrupted while waiting for lock on email: `test@test.com`. Details: "));

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(redissonClient, never()).getBucket(anyString());
        verify(rbucket, never()).get();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(rbucket, never()).delete();
        verify(rlock, never()).unlock();
    }

    @Test
    void shouldNotValidateAndConsumeOtpWhenOtpIsNull() throws InterruptedException {
        when(redissonClient.getLock("lock:otp:test@test.com")).thenReturn(rlock);
        when(rlock.tryLock(5, TimeUnit.SECONDS)).thenReturn(true);
        when(redissonClient.<String>getBucket("otp:test@test.com")).thenReturn(rbucket);
        when(rbucket.get()).thenReturn(null);
        when(rlock.isHeldByCurrentThread()).thenReturn(true);

        boolean result = otpService.validateAndConsumeOtp("test@test.com", "otp");

        assertFalse(result);

        verify(redissonClient).getLock("lock:otp:test@test.com");
        verify(rlock).tryLock(5, TimeUnit.SECONDS);
        verify(redissonClient).getBucket("otp:test@test.com");
        verify(rbucket).get();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(rbucket, never()).delete();
        verify(rlock).isHeldByCurrentThread();
        verify(rlock).unlock();
    }
}
