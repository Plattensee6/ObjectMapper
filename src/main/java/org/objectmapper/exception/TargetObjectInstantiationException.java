package org.objectmapper.exception;

public class TargetObjectInstantiationException extends MappingException{

    public TargetObjectInstantiationException(String message) {
        super(message);
    }
    public TargetObjectInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
