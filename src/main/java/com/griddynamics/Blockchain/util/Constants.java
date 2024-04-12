package com.griddynamics.Blockchain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing various constants used in the blockchain application.
 */
@Getter
@AllArgsConstructor
public enum Constants {

    /**
     * The maximum length of data allowed in a block.
     */
    MAX_DATA_LENGTH(30),

    /**
     * The value representing the number 3.
     */
    THREE(3),

    /**
     * The value representing the number 5.
     */
    FIVE(5),

    /**
     * The value representing the number 100.
     */
    HUNDRED(100),

    /**
     * The value representing the number 300.
     */
    THREE_HUNDRED(300),

    /**
     * The length of the cryptographic key used.
     */
    KEY_LENGTH(1024),

    /**
     * The hexadecimal value 0xff.
     */
    HEX_255(0xff);

    /**
     * The integer value associated with the constant.
     */
    private final int value;
}
