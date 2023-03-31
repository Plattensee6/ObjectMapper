package org.objectmapper.exception;

public class TargetSetterMethodNotFound extends MappingException{
    public TargetSetterMethodNotFound(String message) {
        super(message);
    }

    public TargetSetterMethodNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
