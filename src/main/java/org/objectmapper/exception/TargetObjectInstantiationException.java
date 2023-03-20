package org.objectmapper.exception;

public class ObjectInstantiationException extends MappingException{

    public ObjectInstantiationException(String message) {
        super(message);
    }
    public ObjectInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
