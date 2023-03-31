package org.objectmapper.strategy;

import java.lang.reflect.Field;

public interface FieldInsertionStrategy {
    void insertValue(Object sourceValue, Object targetObject, Field targetField);
}
