package com.griddynamics.Blockchain.blockchain;

import com.griddynamics.Blockchain.person.User;
import com.griddynamics.Blockchain.util.Constants;
import com.griddynamics.Blockchain.util.Message;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a blockchain consisting of multiple blocks.
 */
public final class Blockchain {

    // Fields

    /**
     * The singleton instance of the blockchain.
     */
    private static volatile Blockchain instance;

    /**
     * The current ID for blocks.
     */
    private long currentId = 1;

    /**
     * The list of blocks in the blockchain.
     */
    @Getter
    private List<Block> blocks = new ArrayList<>();

    /**
     * The number of leading zeros required in the hash of a block.
     */
    @Getter
    private int numZeros = 0;

    /**
     * The current data stored in the blockchain.
     */
    @Getter
    private StringBuilder currentData = new StringBuilder();

    /**
     * The ID for messages.
     */
    private int messageId = 0;

    /**
     * The map of client keys.
     */
    @Getter
    private final ConcurrentMap<String, byte[]> clientKeys
            = new ConcurrentHashMap<>();

    /**
     * The map of clients.
     */
    @Getter
    private final ConcurrentMap<String, User> clients
            = new ConcurrentHashMap<>();

    // Constructors

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private Blockchain() {

    }

    // Methods

    /**
     * Returns the singleton instance of the blockchain.
     *
     * @return The singleton instance of the blockchain.
     */
    public static Blockchain getInstance() {
        if (instance == null) {
            synchronized (Blockchain.class) {
                if (instance == null) {
                    instance = new Blockchain();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the ID for the next message.
     *
     * @return The ID for the next message.
     */
    public synchronized int getMessageId() {
        return messageId;
    }

    /**
     * Returns the size of the blockchain.
     *
     * @return The size of the blockchain.
     */
    public synchronized int size() {
        return blocks.size();
    }

    /**
     * Registers a new user/client in the blockchain.
     *
     * @param client The user/client to register.
     */
    public void register(final User client) {
        byte[] publicKey = client.generateKey();
        clientKeys.put(client.getName(), publicKey);
        clients.put(client.getName(), client);
    }

    /**
     * Sends a message within the blockchain network.
     *
     * @param message The message to send.
     */
    public synchronized void send(final Message message) {
        try {
            if (validate(message)
                    && currentData.length()
                    < Constants.MAX_DATA_LENGTH.getValue()
            ) {
                currentData.append(message.content()).append("\n");
                messageId++;
                clients.get(message.destination()).add(message.amount());
            }
        } catch (Exception e) {
            System.err.println("Error validating message");
            throw new RuntimeException(e);
        }

    }

    /**
     * Validates a message using its signature and content.
     *
     * @param message The message to validate.
     * @return True if the message is valid, false otherwise.
     * @throws Exception If an error occurs during validation.
     */
    private boolean validate(final Message message) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(message.sender()));
        byte[] data = (message.id()
                + message.content()).getBytes(StandardCharsets.UTF_8);
        sig.update(data);
        return sig.verify(message.signature())
                && message.id() == getMessageId();
    }

    /**
     * Retrieves the public key for a given client.
     *
     * @param client The name of the client.
     * @return The public key of the client.
     * @throws Exception If an error occurs during key retrieval.
     */
    private PublicKey getPublic(final String client) throws Exception {
        byte[] publicKey = clientKeys.get(client);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * Selects a client from the blockchain network,
     * excluding a specified client.
     *
     * @param exclude The name of the client to exclude.
     * @return The name of the selected client.
     */
    public String getClient(final String exclude) {
        Set<String> clientKeySet = this.clientKeys.keySet();
        Object[] clientsArray = clientKeySet.toArray();
        String to;
        do {
            to = (String) clientsArray[new Random()
                    .nextInt(0, clientsArray.length)];
        } while (exclude.equals(to));
        return to;
    }

    /**
     * Generates a new block with the given creator and current data.
     *
     * @param creator The creator of the block.
     * @return The generated block.
     */
    public Block generateBlock(final String creator) {
        String previousHash = blocks.isEmpty()
                ? "0"
                : blocks.get(blocks.size() - 1).getHash();
        Block block = new Block(currentId, previousHash, creator,
                currentData.toString().isEmpty()
                ? "No Transactions\n"
                : currentData.toString()
        );
        currentData.setLength(0);
        return block;
    }

    /**
     * Adds a new block to the blockchain if it is valid.
     *
     * @param block The block to add.
     * @return True if the block is added successfully, false otherwise.
     */
    public synchronized boolean addBlock(final Block block) {
        if (validate(block)) {
            blocks.add(block);
            currentId++;
            if (numZeros < Constants.THREE.getValue()) {
                numZeros++;
                block.setInfo("N was increased to " + numZeros + "\n");
            } else if (numZeros > Constants.FIVE.getValue()) {
                numZeros--;
                block.setInfo("N was decreased by 1");
            } else {
                block.setInfo("N stays the same");
            }
            return true;
        }
        return false;
    }

    /**
     * Validates a block by comparing its previous hash with the current hash.
     *
     * @param block The block to validate.
     * @return True if the block is valid, false otherwise.
     */
    private boolean validate(final Block block) {
        String currentHash = blocks.isEmpty()
                ? "0"
                : blocks.get(blocks.size() - 1).getHash();
        return currentHash.equals(block.getPreviousHash());
    }

    /**
     * Returns a string representation of the blockchain.
     *
     * @return A string representation of the blockchain.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            sb.append(block.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
