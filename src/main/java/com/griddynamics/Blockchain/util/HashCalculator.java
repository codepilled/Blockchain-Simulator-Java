package com.griddynamics.Blockchain.util;

import com.griddynamics.Blockchain.blockchain.Block;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Utility class for calculating the hash of a block.
 */
public class HashCalculator {

    // Fields

    /**
     * Random number generator instance.
     */
    private static final Random RANDOM = new Random();

    // Methods

    /**
     * Calculates the hash of the given block with
     * the specified number of leading zeros.
     *
     * @param block    The block for which to calculate the hash.
     * @param numZeros The number of leading zeros required in the hash.
     */
    public static synchronized void calculateHash(final Block block,
                                                  final int numZeros) {
        try {
            String hash;
            do {
                block.setMagicNumber(RANDOM.nextLong());
                hash = calculateHash(block.getRepresentation());
            } while (!isHashValid(hash, numZeros));
            block.setHash(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the given hash has the required number of leading zeros.
     *
     * @param hash     The hash to check.
     * @param numZeros The number of leading zeros required.
     * @return True if the hash has the required number of leading zeros,
     * false otherwise.
     */
    private static boolean isHashValid(final String hash,
                                       final int numZeros) {
        return hash.startsWith("0".repeat(numZeros));
    }

    /**
     * Calculates the SHA-256 hash of the given block representation.
     *
     * @param block The block representation for which to calculate the hash.
     * @return The SHA-256 hash of the block representation.
     * @throws NoSuchAlgorithmException If SHA-256 algorithm is not available.
     */
    private static String calculateHash(final String block
    ) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(block.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(
                        Constants.HEX_255.getValue() & elem
                );
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
    }
}
