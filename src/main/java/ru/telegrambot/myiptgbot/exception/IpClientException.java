package ru.telegrambot.myiptgbot.exception;

public class IpClientException extends RuntimeException {

    public IpClientException(String message) {
        super(message);
    }

    public IpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
