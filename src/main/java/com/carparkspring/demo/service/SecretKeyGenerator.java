package com.carparkspring.demo.service;

import java.util.Base64;
import java.security.SecureRandom;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 32 bytes = 256 bits
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        System.out.println(encodedKey);
    }
}