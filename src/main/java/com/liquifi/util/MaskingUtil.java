package com.liquifi.util;

import org.springframework.stereotype.Component;

@Component
public class MaskingUtil {

    public String maskPan(String pan) {
        if (pan == null || pan.length() < 4) return "****";
        return "XXXXX" + pan.substring(5, 9) + "X";
    }

    public String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 4) return "****";
        return mobile.substring(0, 2) + "XXXXXX" + mobile.substring(mobile.length() - 2);
    }

    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "****";
        String[] parts = email.split("@");
        String local = parts[0];
        String visible = local.length() > 2 ? local.substring(0, 2) : local.substring(0, 1);
        return visible + "****@" + parts[1];
    }
}
