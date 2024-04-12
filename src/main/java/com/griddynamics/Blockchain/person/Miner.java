package com.griddynamics.Blockchain.person;

import com.griddynamics.Blockchain.blockchain.Blockchain;
import com.griddynamics.Blockchain.blockchain.Block;
import com.griddynamics.Blockchain.util.Constants;
import com.griddynamics.Blockchain.util.HashCalculator;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

/**
 * Represents a miner in the blockchain network.
 */
public final class Miner extends User {

    // Constructors

    /**
     * Constructs a new Miner with the given name.
     *
     * @param name The name of the miner.
     */
    public Miner(final String name) {
        super(name);
    }

    // Methods

    /**
     * Runs miner's operation, either sending messages or mining a new block.
     *
     * @param blockchain The blockchain instance the miner operates on.
     */

    public void run(final Blockchain blockchain) {
        int num = new Random().nextInt(0, 2);
        if (num == 1) {
            sendMessages(blockchain);
        } else {
            mine(blockchain);
        }
    }

    /**
     * Mines a new block in the blockchain.
     *
     * @param blockchain The blockchain instance to mine on.
     */
    private void mine(final Blockchain blockchain) {
        Block newBlock = blockchain.generateBlock(getName());
        int numZeros = blockchain.getNumZeros();
        Instant start = Instant.now();
        HashCalculator.calculateHash(newBlock, numZeros);
        Duration timeTaken = Duration.between(start, Instant.now());
        newBlock.setTimeTaken(timeTaken.toSeconds());
        if (blockchain.addBlock(newBlock)) {
            add(Constants.HUNDRED.getValue());
        }
    }
}
