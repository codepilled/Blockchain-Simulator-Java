package com.griddynamics.Blockchain.blockchain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a block in a blockchain.
 */
public final class Block {

    // Fields

    /**
     * The unique identifier of the block.
     */
    @Getter
    private final long id;

    /**
     * The epoch time when the block was created.
     */
    private final long epochTime;

    /**
     * The creator of the block.
     */
    @Getter
    private final String creator;

    /**
     * The data stored in the block.
     */
    private final String data;

    /**
     * The hash of the previous block in the blockchain.
     */
    @Getter
    private final String previousHash;

    /**
     * The hash of the current block.
     */
    @Getter
    @Setter
    private String hash;

    /**
     * The magic number associated with the block.
     */
    @Getter
    @Setter
    private long magicNumber;

    /**
     * The time taken to generate the block, in seconds.
     */
    @Setter
    private long timeTaken;

    /**
     * Additional information about the block.
     */
    @Setter
    private String info;

    // Constructors

    /**
     * Constructs a new block with the given parameters.
     *
     * @param blockId       The unique identifier of the block.
     * @param prevHash      The hash of the previous block in the blockchain.
     * @param blockCreator  The creator of the block.
     * @param blockData     The data stored in the block.
     */
    public Block(final long blockId, final String prevHash,
                 final String blockCreator, final String blockData) {
        id = blockId;
        epochTime = Instant.now().toEpochMilli();
        previousHash = prevHash;
        creator = blockCreator;
        data = blockData;
    }

    // Methods

    /**
     * Returns a string representation of the block.
     *
     * @return A string representation of the block.
     */
    @Override
    public String toString() {
        return String.format("""
            Block:
            Created by miner %s
            %s gets 100VC
            Id: %d
            Timestamp: %s
            Magic number: %d
            Hash of the previous block: \s
            %s
            Hash of the block: \s
            %s
            Block data:
            %sBlock was generating for %d seconds
            %s
            """,
                this.creator, this.creator,
                this.id, this.epochTime,
                this.magicNumber, this.previousHash,
                this.hash, this.data,
                this.timeTaken, this.info
        );
    }

    /**
     * Returns a string representation of the block's data.
     *
     * @return A string representation of the block's data.
     */
    public String getRepresentation() {
        return this.previousHash + this.id + this.magicNumber + this.epochTime;
    }
}
