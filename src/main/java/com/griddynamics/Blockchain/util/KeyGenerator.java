package com.griddynamics.Blockchain.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for generating RSA key pairs.
 */
public final class KeyGenerator {

    // Fields

    /**
     * The key pair generator instance.
     */
    private final KeyPairGenerator keyGen;

    /**
     * The private key generated.
     */
    private PrivateKey privateKey;

    /**
     * The public key generated.
     */
    private PublicKey publicKey;

    // Constructors

    /**
     * Constructs a KeyGenerator with the specified key length.
     *
     * @param length The length of the key to generate.
     */
    public KeyGenerator(final int length) {
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(length);
            createKeys();
        } catch (Exception e) {
            System.err.println("Error generating key.");
            throw new RuntimeException(e);
        }
    }

    // Methods

    /**
     * Generates the key pair.
     */
    private void createKeys() {
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    /**
     * Gets the byte array representation of the private key.
     *
     * @return The byte array representation of the private key.
     */
    public byte[] getPrivateKey() {
        return privateKey.getEncoded();
    }

    /**
     * Gets the byte array representation of the public key.
     *
     * @return The byte array representation of the public key.
     */
    public byte[] getPublicKey() {
        return publicKey.getEncoded();
    }
}
