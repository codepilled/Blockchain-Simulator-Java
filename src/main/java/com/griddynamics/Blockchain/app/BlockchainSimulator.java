package com.griddynamics.Blockchain.app;

import com.griddynamics.Blockchain.blockchain.Blockchain;
import com.griddynamics.Blockchain.person.Miner;
import com.griddynamics.Blockchain.person.User;
import com.griddynamics.Blockchain.util.Constants;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the simulation of the blockchain network.
 */
public final class BlockchainSimulator {

    // Fields

    /**
     * Executor service for managing miners.
     */
    private static final ExecutorService MINER_SERVICE
            = Executors.newFixedThreadPool(5);

    /**
     * Executor service for managing users.
     */
    private static final ExecutorService USER_SERVICE
            = Executors.newFixedThreadPool(4);

    /**
     * List of usernames.
     */
    private static final List<String> USERS
            = List.of("John", "Jack", "Anna", "Paul");

    /**
     * List of miner names.
     */
    private static final List<String> MINERS
            = List.of("Miner1", "Miner2", "Miner3", "Miner4", "Miner5");

    // Methods

    /**
     * Main method to start the blockchain simulation.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        BlockchainSimulator blockchainSimulator = new BlockchainSimulator();
        blockchainSimulator.startSimulation();
    }

    /**
     * Starts the blockchain simulation.
     */
    public void startSimulation() {
        Blockchain blockchain = Blockchain.getInstance();
        int numberOfBlocks = getNumberOfBlocks();
        registerUsers(blockchain, numberOfBlocks);
        registerMiners(blockchain, numberOfBlocks);
        waitForBlockchainToFill(blockchain, numberOfBlocks);
        MINER_SERVICE.shutdown();
        USER_SERVICE.shutdown();
        System.out.println(blockchain);
    }

    /**
     * Registers users in the blockchain network.
     *
     * @param blockchain    The blockchain instance to register users with.
     * @param numberOfBlocks The number of blocks to be generated.
     */
    private void registerUsers(final Blockchain blockchain,
                               final int numberOfBlocks) {
        for (String userName : USERS) {
            USER_SERVICE.execute(() -> {
                User user = new User(userName);
                blockchain.register(user);
                while (blockchain.size() < numberOfBlocks) {
                    user.sendMessages(blockchain);
                }
            });
        }
    }

    /**
     * Registers miners in the blockchain network.
     *
     * @param blockchain    The blockchain instance to register miners with.
     * @param numberOfBlocks The number of blocks to be generated.
     */
    private void registerMiners(final Blockchain blockchain,
                                final int numberOfBlocks) {
        for (String minerName : MINERS) {
            MINER_SERVICE.execute(() -> {
                Miner miner = new Miner(minerName);
                blockchain.register(miner);
                while (blockchain.size() < numberOfBlocks) {
                    miner.run(blockchain);
                }
            });
        }
    }

    /**
     * Prompts the user to enter the required number of blocks to be generated.
     *
     * @return The required number of blocks.
     */
    private int getNumberOfBlocks() {
        System.out.print("Enter required number of Blocks to be generated: ");
        return new Scanner(System.in).nextInt();
    }

    /**
     * Waits for the blockchain to fill with the required number of blocks.
     *
     * @param blockchain    The blockchain instance to wait for.
     * @param numberOfBlocks The number of blocks to wait for.
     */
    private void waitForBlockchainToFill(final Blockchain blockchain,
                                         final int numberOfBlocks) {
        while (blockchain.size() < numberOfBlocks) {
            try {
                Thread.sleep(Constants.HUNDRED.getValue());
            } catch (InterruptedException e) {
                System.err.println("InterruptedException occurred: "
                        + e.getMessage());
            }
        }
    }
}
