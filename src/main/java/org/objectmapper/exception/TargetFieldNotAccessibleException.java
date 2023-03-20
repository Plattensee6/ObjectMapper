package org.objectmapper.exception;

public class TargetFieldNotAccessibleException extends MappingException{
    public TargetFieldNotAccessibleException(String message) {
        super(message);
    }

    public TargetFieldNotAccessibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
