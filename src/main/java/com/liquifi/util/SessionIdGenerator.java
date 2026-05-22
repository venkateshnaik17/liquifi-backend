package com.liquifi.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Component
public class SessionIdGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a cryptographically secure, URL-safe session ID.
     * Format: UUID-prefix + base64-random-suffix
     */
    public String generate() {
        byte[] randomBytes = new byte[24];
        SECURE_RANDOM.nextBytes(randomBytes);
        String random = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return UUID.randomUUID().toString().replace("-", "") + "-" + random;
    }
}
