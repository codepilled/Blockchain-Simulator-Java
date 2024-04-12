package com.griddynamics.Blockchain.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {
    private KeyGenerator keyGenerator;

    @BeforeEach
    void setUp() {
        keyGenerator = new KeyGenerator(2048); // Using a standard key length for RSA
    }

    @Test
    void testKeyGeneratorConstructor() {
        assertNotNull(keyGenerator);
    }

    @Test
    void testGetPrivateKey() {
        byte[] privateKeyBytes = keyGenerator.getPrivateKey();
        assertNotNull(privateKeyBytes);
    }

    @Test
    void testGetPublicKey() throws Exception {
        byte[] publicKeyBytes = keyGenerator.getPublicKey();
        assertNotNull(publicKeyBytes);

        // Convert byte array to PublicKey object
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Verify that the public key is valid
        assertNotNull(publicKey);
    }

}