package org.objectmapper.exception;

/**
 A general exception that can be thrown when there is an error during the mapping process.
 */
public class MappingException extends RuntimeException{
    public MappingException() {
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
