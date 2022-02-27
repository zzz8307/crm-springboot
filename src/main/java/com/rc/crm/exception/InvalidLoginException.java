package com.rc.crm.exception;

/**
 * @author rc
 */
public class InvalidLoginException extends Exception{
    public InvalidLoginException() {
        super();
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
