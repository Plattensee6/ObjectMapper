package org.objectmapper.exception;

/**
 This exception is thrown when the access to a target field is denied or restricted, and cannot be accessed through reflection.
 It extends the MappingException class and represents an issue encountered during mapping.
 */
public class TargetFieldNotAccessibleException extends MappingException{
    public TargetFieldNotAccessibleException(String message) {
        super(message);
    }

    public TargetFieldNotAccessibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
