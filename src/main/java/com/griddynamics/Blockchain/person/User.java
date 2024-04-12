package com.griddynamics.Blockchain.person;

import com.griddynamics.Blockchain.blockchain.Blockchain;
import com.griddynamics.Blockchain.util.Constants;
import com.griddynamics.Blockchain.util.Message;
import com.griddynamics.Blockchain.util.KeyGenerator;
import lombok.Getter;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Random;

/**
 * Represents a user in the blockchain network.
 */
public class User {

    // Fields

    /**
     * The name of the user.
     */
    @Getter
    private final String name;

    /**
     * Random number generator instance.
     */
    private static final Random RANDOM = new Random();

    /**
     * The private key of the user.
     */
    private byte[] privateKey;

    /**
     * The total amount of virtual currency (VC) the user possesses.
     */
    @Getter
    private int totalAmount = Constants.HUNDRED.getValue();


    // Constructors

    /**
     * Constructs a new User with the given name.
     *
     * @param userName The name of the user.
     */
    public User(final String userName) {
        name = userName;
    }

    /**
     * Sends messages within the blockchain network.
     *
     * @param blockchain The blockchain to send messages within.
     */
    public void sendMessages(final Blockchain blockchain) {
        try {
            Thread.sleep(RANDOM.nextInt(
                    Constants.THREE_HUNDRED.getValue()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (totalAmount == 0) {
            return;
        }
        int id = blockchain.getMessageId();
        String otherClient = blockchain.getClient(name);
        Message message = generateMessage(id, otherClient);
        blockchain.send(message);
    }

    /**
     * Generates a message to be sent within the blockchain network.
     *
     * @param id           The ID of the message.
     * @param otherClient  The recipient of the message.
     * @return The generated message.
     */
    private Message generateMessage(final int id,
                                    final String otherClient) {
        try {
            int amount = RANDOM.nextInt(1, totalAmount + 1);
            String message = name + " sent " + amount + " VC to " + otherClient;
            byte[] signature = sign(id + message);
            totalAmount -= amount;
            return new Message(name, message, id,
                    signature, otherClient, amount);
        } catch (Exception e) {
            System.err.println("Error signing the message.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Signs the provided data using the user's private key.
     *
     * @param data The data to sign.
     * @return The signature of the data.
     * @throws Exception If an error occurs during signing.
     */
    private byte[] sign(final String data) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(privateKey));
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    /**
     * Retrieves the private key of the user.
     *
     * @param privateKeyByte The byte array representation of the private key.
     * @return The private key of the user.
     * @throws Exception If an error occurs during key retrieval.
     */
    public PrivateKey getPrivate(final byte[] privateKeyByte
    ) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Generates a new public-private key pair for the user.
     *
     * @return The public key of the user.
     */
    public byte[] generateKey() {
        KeyGenerator keyGen = new KeyGenerator(
                Constants.KEY_LENGTH.getValue()
        );
        privateKey = keyGen.getPrivateKey();
        return keyGen.getPublicKey();
    }

    /**
     * Increases the total amount of virtual currency (VC) owned by the user.
     *
     * @param amount The amount to add.
     */
    public void add(final int amount) {
        totalAmount += amount;
    }
}
