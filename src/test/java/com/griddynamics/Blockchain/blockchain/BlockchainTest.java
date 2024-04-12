package com.griddynamics.Blockchain.blockchain;

import com.griddynamics.Blockchain.person.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlockchainTest {

    private Blockchain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = Blockchain.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Reset the state of the Blockchain instance after each test
        blockchain.getBlocks().clear();
    }


    @Test
    void testRegisterUser() {
        // Create a mock user
        User user = mock(User.class);
        when(user.getName()).thenReturn("TestUser");
        when(user.generateKey()).thenReturn(new byte[]{1, 2, 3});

        blockchain.register(user);

        // Verify that the user is registered
        assertTrue(blockchain.getClientKeys().containsKey("TestUser"));
        assertTrue(blockchain.getClients().containsKey("TestUser"));
    }

    @Test
    void testGenerateBlock() {

        // Generate a block
        Block block = blockchain.generateBlock("Creator");

        // Verify that the block is generated correctly
        assertNotEquals(block, null);
        assertEquals(block.getCreator(), "Creator");
    }

    @Test
    void testAddBlock() {
        // Create and add mock blocks to the blockchain
        int numBlocks = 15;
        StringBuilder expectedBlockchainString = new StringBuilder();
        for (int i = 0; i < numBlocks; i++) {
            Block block = mock(Block.class);
            String previousHash = (i == 0) ? "0" : String.valueOf(i);
            String hash = String.valueOf(i + 1);
            String blockToString = "Block" + i;
            expectedBlockchainString.append(blockToString).append("\n");

            when(block.getPreviousHash()).thenReturn(previousHash);
            when(block.getId()).thenReturn((long) (i + 1));
            when(block.getHash()).thenReturn(hash);
            when(block.toString()).thenReturn(blockToString);

            boolean added = blockchain.addBlock(block);
            assertTrue(added);
        }

        // Verify the size and string representation of the blockchain
        assertEquals(blockchain.size(), numBlocks);
        assertEquals(blockchain.toString(), expectedBlockchainString.toString());
    }

}