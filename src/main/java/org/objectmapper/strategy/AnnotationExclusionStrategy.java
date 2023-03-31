package org.objectmapper.strategy;

import org.objectmapper.annotation.ExcludeFromMapping;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Implementation of the {@link FieldExclusionStrategy} interface that excludes fields
 * annotated with the {@link org.objectmapper.annotation.ExcludeFromMapping} annotation.
 */
public class AnnotationExclusionStrategy implements FieldExclusionStrategy {
    @Override
    public Stream<Field> filter(Stream<Field> declaredFields) {
        if (Objects.isNull(declaredFields)){
            throw new IllegalArgumentException("DeclaredFields parameter cannot be null in filter method.");
        }
        Predicate<Field> isAnnotationNotPresent = getFieldNotExcludedPredicate();
        return declaredFields.filter(isAnnotationNotPresent);
    }
    private Predicate<Field> getFieldNotExcludedPredicate(){
        return field -> !field.isAnnotationPresent(ExcludeFromMapping.class);
    }
}
