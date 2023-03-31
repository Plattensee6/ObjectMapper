package org.objectmapper.strategy;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This interface provides a strategy for excluding fields from being mapped by the object mapper.
 * The strategy is implemented by a function that takes a {@link Field} and returns a {@link Predicate}
 * that evaluates to {@code true} if the field should be excluded from mapping.
 */
public interface FieldExclusionStrategy {
    Stream<Field> filter(Stream<Field> declaredFields);
}
