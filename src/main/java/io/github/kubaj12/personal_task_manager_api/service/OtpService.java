package io.github.kubaj12.personal_task_manager_api.service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.kubaj12.personal_task_manager_api.config.OtpConfigProperties;
import io.github.kubaj12.personal_task_manager_api.exception.OtpLockAcquisitionException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OtpService {
    final PasswordEncoder passwordEncoder;
    private final RedissonClient redissonClient;
    final OtpConfigProperties otpConfigProperties;


    String generateAndStoreOtp(String email) {
        RLock lock = redissonClient.getLock("lock:otp:" + email);
        boolean acquired = false;
        String otp = null;

        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!acquired) {
                throw new OtpLockAcquisitionException("Failed to acquire lock for email " + email + " within the timeout period.");
            }
            otp = this.generateOtp(otpConfigProperties.length());
            String hashed_otp = passwordEncoder.encode(otp);
            redissonClient.getBucket("otp:" + email).set(hashed_otp, otpConfigProperties.ttl());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OtpLockAcquisitionException("Thread was interrupted while waiting for lock on email: " + email + ". Details: " + e.getMessage());
        }
        finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return otp;
    }

    Boolean validateAndConsumeOtp(String email, String otp) {
        RLock lock = redissonClient.getLock("lock:otp:" + email);
        boolean acquired = false;
        boolean result = false;
        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!acquired) {
                throw new OtpLockAcquisitionException("Failed to acquire lock for email " + email + " within the timeout period.");
            }
            RBucket<String> bucket = redissonClient.getBucket("otp:" + email);
            String hashedOtp = bucket.get();
            if (hashedOtp == null) return false;
            if (passwordEncoder.matches(otp, hashedOtp)) {
                bucket.delete();
                result = true;
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OtpLockAcquisitionException("Thread was interrupted while waiting for lock on email: `" + email + "`. Details: " + e.getMessage());
        }
        finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return result;
    }

    private String generateOtp(int otpLength) {
        if (0 >= otpLength) {
            throw new IllegalArgumentException("The length must be greater than zero.");
        }
        final String ALPHANUMERIC_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom sr = new SecureRandom();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            int index = sr.nextInt(ALPHANUMERIC_CHARS.length());
            sb.append(ALPHANUMERIC_CHARS.charAt(index));
        }

        return sb.toString();
    }
}
