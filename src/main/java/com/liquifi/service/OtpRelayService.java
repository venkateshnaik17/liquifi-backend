package com.liquifi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Relays the user-entered OTP to the Selenium worker via Redis.
 * OTP is stored temporarily with a short TTL and deleted after consumption.
 * OTP is NEVER persisted to the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpRelayService {

    private final StringRedisTemplate redisTemplate;

    @Value("${liquifi.otp.ttl-seconds:300}")
    private int otpTtlSeconds;

    @Value("${liquifi.redis.otp-key-prefix}")
    private String otpKeyPrefix;

    /**
     * Stores the OTP in Redis with a TTL. The automation worker polls this key.
     */
    public void relayOtp(String sessionId, String otp) {
        String key = otpKeyPrefix + sessionId;
        redisTemplate.opsForValue().set(key, otp, Duration.ofSeconds(otpTtlSeconds));
        log.info("OTP relayed to Redis for session={}. TTL={}s", sessionId, otpTtlSeconds);
    }

    /**
     * Called by automation worker to consume the OTP. Deletes it after reading.
     */
    public String consumeOtp(String sessionId) {
        String key = otpKeyPrefix + sessionId;
        String otp = redisTemplate.opsForValue().getAndDelete(key);
        if (otp != null) {
            log.info("OTP consumed from Redis for session={}", sessionId);
        }
        return otp;
    }

    public boolean hasOtp(String sessionId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(otpKeyPrefix + sessionId));
    }

    public void deleteOtp(String sessionId) {
        redisTemplate.delete(otpKeyPrefix + sessionId);
        log.info("OTP explicitly deleted for session={}", sessionId);
    }
}
