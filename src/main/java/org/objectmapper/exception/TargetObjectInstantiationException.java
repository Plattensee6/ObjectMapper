package org.objectmapper.exception;

/**
 * Exception thrown when an error occurs during the instantiation of a target object.
 */
public class TargetObjectInstantiationException extends MappingException{

    public TargetObjectInstantiationException(String message) {
        super(message);
    }
    public TargetObjectInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
