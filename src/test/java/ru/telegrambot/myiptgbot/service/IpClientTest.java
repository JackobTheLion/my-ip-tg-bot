package ru.telegrambot.myiptgbot.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IpClientTest {

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
