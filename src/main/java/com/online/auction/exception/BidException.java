package com.online.auction.exception;

public class BidException extends RuntimeException {
    public BidException() {
        super();
    }

    public BidException(String message) {
        super(message);
    }

    public BidException(String message, Throwable cause) {
        super(message, cause);
    }

    public BidException(Throwable cause) {
        super(cause);
    }
}