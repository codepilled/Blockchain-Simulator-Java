package com.griddynamics.Blockchain.app;

import com.griddynamics.Blockchain.blockchain.Blockchain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockchainSimulatorTest {


    @Mock
    private Blockchain blockchain;

    private BlockchainSimulator blockchainSimulator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blockchain = Blockchain.getInstance();
        String numOfBlocks = "10";
        ByteArrayInputStream inputStream = new ByteArrayInputStream((numOfBlocks + "\n").getBytes());
        System.setIn(inputStream);
        blockchainSimulator = new BlockchainSimulator();
    }

    @AfterEach
    void tearDown() {
        System.setIn(System.in);
    }

    @Test
    void testStartSimulation() {

        blockchainSimulator.startSimulation();
        assertEquals(blockchain.size(), 10);
    }

}