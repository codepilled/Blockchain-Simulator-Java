package com.griddynamics.Blockchain.util;

/**
 * Represents a message with sender, content, ID, signature,
 * destination, and amount.
 *
 * @param sender      The sender of the message.
 * @param content     The content of the message.
 * @param id          The ID of the message.
 * @param signature   The signature of the message.
 * @param destination The destination of the message.
 * @param amount      The amount associated with the message.
 */
public record Message(String sender,
                      String content,
                      int id,
                      byte[] signature,
                      String destination,
                      int amount) {
}
