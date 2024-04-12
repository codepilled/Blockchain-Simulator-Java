package com.griddynamics.Blockchain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    @Test
    void testMessageConstructorAndGetters() {
        // Given
        String sender = "Alice";
        String content = "Hello, Bob!";
        int id = 123;
        byte[] signature = "signature".getBytes();
        String destination = "Bob";
        int amount = 100;

        // When
        Message message = new Message(sender, content, id, signature, destination, amount);

        // Then
        assertEquals(message.sender(), sender);
        assertEquals(message.content(), content);
        assertEquals(message.id(), id);
        assertEquals(message.signature(), signature);
        assertEquals(message.destination(), destination);
        assertEquals(message.amount(), amount);
    }
}