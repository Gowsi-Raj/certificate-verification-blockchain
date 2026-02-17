package com.sih.certificate.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {

    // SHA256 helper
    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }

    // normalize text (remove spaces + lowercase)
    public static String norm(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", "").toLowerCase();
    }
}
