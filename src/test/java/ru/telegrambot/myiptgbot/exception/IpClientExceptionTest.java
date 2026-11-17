package ru.telegrambot.myiptgbot.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class IpClientExceptionTest {

    @Test
    void ipClientException_isRuntimeException() {
        IpClientException exc = new IpClientException("test");
        assertInstanceOf(RuntimeException.class, exc);
        assertEquals("test", exc.getMessage());
    }

    @Test
    void ipClientException_withCause() {
        Throwable cause = new RuntimeException("root");
        IpClientException exc = new IpClientException("test", cause);
        assertEquals(cause, exc.getCause());
    }
}
