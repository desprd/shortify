package com.iljaproject.shortify.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    void longNumber_encodeWithBase62_encodedString() {
        // Given
        long number = 123456789;

        // When
        String encoded = Base62Encoder.encode(number);

        // Then
        assertEquals("8M0kX", encoded);
    }
}
