package com.griddynamics.Blockchain.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ConstantsTest {
    @Test
    void testConstantsValues() {
        // Check the values of each constant
        assertEquals(Constants.MAX_DATA_LENGTH.getValue(), 30);
        assertEquals(Constants.THREE.getValue(), 3);
        assertEquals(Constants.FIVE.getValue(), 5);
        assertEquals(Constants.HUNDRED.getValue(), 100);
        assertEquals(Constants.THREE_HUNDRED.getValue(), 300);
        assertEquals(Constants.KEY_LENGTH.getValue(), 1024);
        assertEquals(Constants.HEX_255.getValue(), 0xff);
    }

}