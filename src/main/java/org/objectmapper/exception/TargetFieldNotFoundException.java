package org.objectmapper.exception;

/**
 * Represents an exception that is thrown when a target field is not found during object mapping.
 */
public class TargetFieldNotFoundException extends MappingException{
    public TargetFieldNotFoundException(String message) {
        super(message);
    }

    public TargetFieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
