package com.hamropatro.exceptions;

public class VoteUpdateWindowExpiredException extends RuntimeException {
    public VoteUpdateWindowExpiredException(String message) {
        super(message);
    }
}
