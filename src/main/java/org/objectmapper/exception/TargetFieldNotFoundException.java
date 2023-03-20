package org.objectmapper.exception;

public class TargetFieldNotFoundException extends MappingException{
    public TargetFieldNotFoundException(String message) {
        super(message);
    }

    public TargetFieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
