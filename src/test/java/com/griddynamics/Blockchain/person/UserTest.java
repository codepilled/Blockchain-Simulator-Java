package com.griddynamics.Blockchain.person;

import com.griddynamics.Blockchain.blockchain.Blockchain;
import com.griddynamics.Blockchain.util.Constants;
import com.griddynamics.Blockchain.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserTest {
    @Mock
    private Blockchain blockchain;

    private User user;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(blockchain.getMessageId()).thenReturn(1);
        when(blockchain.getClient(anyString())).thenReturn("Recipient");
        user = new User("Sender");
        user.generateKey();
    }

    @Test
    void testSendMessages() {

        // When
        user.sendMessages(blockchain);

        // Then
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(blockchain).send(messageCaptor.capture());

        Message sentMessage = messageCaptor.getValue();
        assertNotEquals(sentMessage, null);
        assertEquals(sentMessage.sender(), "Sender");
        assertEquals(sentMessage.destination(),"Recipient");
        assertTrue(sentMessage.amount() >= 1 && sentMessage.amount() <= Constants.HUNDRED.getValue());

        // Ensure total amount is deducted after sending message
        assertEquals(user.getTotalAmount(), Constants.HUNDRED.getValue() - sentMessage.amount());
    }

    @Test
    void testSendMessagesWhenNoFunds() {

        user.add(-user.getTotalAmount()); // Set total amount to 0

        // When
        user.sendMessages(blockchain);

        // Then
        verify(blockchain, never()).send(any(Message.class));
        assertEquals(user.getTotalAmount(), 0);
    }

}