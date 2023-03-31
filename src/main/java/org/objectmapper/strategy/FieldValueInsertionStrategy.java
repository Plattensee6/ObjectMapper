package org.objectmapper.strategy;

import java.lang.reflect.Field;

/**
 * Defines a strategy for inserting a value from a source object into a target object field.
 */
public interface FieldValueInsertionStrategy {
    /**
     * Inserts the provided source value into the provided target object field.
     * @param sourceValue  the value to insert into the target field
     * @param targetObject the target object to insert the value into
     * @param targetField  the target field to insert the value into
     */
    void insertValue(Object sourceValue, Object targetObject, Field targetField);
}
