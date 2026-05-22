package com.liquifi.exception;

public class OtpTimeoutException extends RuntimeException {
    public OtpTimeoutException(String sessionId) {
        super("OTP submission timed out for session: " + sessionId);
    }
}
