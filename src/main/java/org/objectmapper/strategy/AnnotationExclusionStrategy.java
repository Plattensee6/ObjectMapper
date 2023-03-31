package org.objectmapper.strategy;

import java.lang.reflect.Field;

public class AnnotationAndPrivateModifierExclusionStrategy implements FieldExclusionStrategy{
    @Override
    public boolean isExcluded(Field field) {
        return false;
    }
}
