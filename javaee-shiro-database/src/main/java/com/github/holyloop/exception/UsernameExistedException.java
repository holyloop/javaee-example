package com.github.holyloop.exception;

public class UsernameExistedException extends Exception {

    private static final long serialVersionUID = -6073718670508192198L;

    public UsernameExistedException() {}

    public UsernameExistedException(String msg) {
        super(msg);
    }
}
