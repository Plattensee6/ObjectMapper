package org.objectmapper.mapper;

import org.objectmapper.annotation.ExcludeFromMapping;
import org.objectmapper.exception.TargetFieldNotAccessibleException;
import org.objectmapper.exception.TargetFieldNotFoundException;
import org.objectmapper.strategy.ObjectFactory;
import org.objectmapper.strategy.TargetObjectFactory;
import org.objectmapper.strategy.AnnotationExclusionStrategy;
import org.objectmapper.strategy.FieldExclusionStrategy;
import org.objectmapper.strategy.FieldValueInsertionStrategy;
import org.objectmapper.strategy.SetterInsertionStrategy;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A utility class that maps fields from a source object to a target object using Java reflection. The source and target
 * objects can be of different types as long as they have the same fields. This implementation provides default strategies
 * for handling excluded fields and field value insertion.
 */
public class ObjectMapperImpl implements ObjectMapper {
    /**
     * The object factory used to create new target objects.
     */
    private final ObjectFactory objectFactory;
    /**
     * The field value insertion strategy used to insert values into the target object fields.
     */
    private final FieldValueInsertionStrategy fieldValueInsertionStrategy;
    /**
     * The field exclusion strategy used to determine which fields to exclude from the mapping.
     */
    private final FieldExclusionStrategy fieldExclusionStrategy;

    /**
     * Constructs an {@code ObjectMapperImpl} object with the given mapping configuration and object factory.
     *
     * @param objectFactory the object factory.
     * @throws IllegalArgumentException if either {@code mappingConfig} or {@code initializer} is null.
     */
    ObjectMapperImpl(ObjectFactory objectFactory,
                     FieldValueInsertionStrategy insertionStrategy,
                     FieldExclusionStrategy exclusionStrategy) {

        this.objectFactory = Objects.requireNonNull(objectFactory,
                "Object initializer cannot be null.");
        this.fieldExclusionStrategy = Objects.requireNonNull(exclusionStrategy,
                "Field exclusion strategy cannot be null.");
        this.fieldValueInsertionStrategy = Objects.requireNonNull(insertionStrategy,
                "Field insertion strategy cannot be null.");
    }

    /**
     * Maps the fields from a source object to a target object using Java reflection.
     *
     * @param source     the source object.
     * @param targetType the target object type.
     * @param <S>        the type of the source object.
     * @param <T>        the type of the target object.
     * @return the target object with the fields mapped from the source object.
     * @throws IllegalArgumentException          if either {@code source} or {@code targetType} is null.
     * @throws TargetFieldNotFoundException      if the target field cannot be found.
     * @throws TargetFieldNotAccessibleException if the target field cannot be accessed.
     */
    @Override
    public <S, T> T mapObject(S source, Class<T> targetType) {
        if (Objects.isNull(source) || Objects.isNull(targetType)) {
            throw new IllegalArgumentException("Invalid parameters! Source object and target type cannot be null.");
        }
        return copySourceFieldsToTarget(source, targetType);
    }

    /**
     * Returns a stream of all fields of the given source object, excluding the ones defined in the fieldExclusionStrategy.
     *
     * @param source the object from which to retrieve the fields
     * @param <S>    the type of the source object
     * @return a stream of all fields of the source object excluding the excluded ones
     */
    private <S> Stream<Field> getFields(S source) {
        return fieldExclusionStrategy.filter(Stream.of(source.getClass().getDeclaredFields()));
    }

    /**
     * Copies fields from the source object to a new instance of the target type, and returns the target object.
     *
     * @param source     The source object from which to copy the fields.
     * @param targetType The type of the target object.
     * @return The target object with the copied fields.
     * @throws TargetFieldNotFoundException      If a field with the same name as the source field cannot be found in the target object.
     * @throws TargetFieldNotAccessibleException If the field in the target object cannot be accessed due to security restrictions.
     * @throws IllegalArgumentException          If either source or targetType is null.
     */
    private <S, T> T copySourceFieldsToTarget(S source, Class<T> targetType) {
        T target = objectFactory.create(targetType);
        getFields(source).forEach(field -> {
            Object sourceValue = getFieldValue(field, source);
            Field targetField = getFieldByName(target, field.getName());
            fieldValueInsertionStrategy.insertValue(sourceValue, target, targetField);
        });
        return target;
    }

    /**
     * This method checks whether the given field should be excluded from mapping based on the current field exclusion strategy,
     * and throws a {@link TargetFieldNotAccessibleException} if the field is excluded based on an {@link AnnotationExclusionStrategy}
     * and is annotated with the {@link ExcludeFromMapping} annotation.
     *
     * @param field the field to check for exclusion
     * @throws TargetFieldNotAccessibleException if the field is excluded based on an AnnotationExclusionStrategy and is annotated with the ExcludeFromMapping annotation
     */

    /**
     * Returns the field with the specified name from the target object.
     *
     * @param target    the target object
     * @param fieldName the name of the field to retrieve
     * @param <T>       the type of the target object
     * @return the field with the specified name from the target object
     * @throws TargetFieldNotFoundException if the field with the specified name cannot be found in the target object
     */
    private <T> Field getFieldByName(T target, String fieldName) {
        try {
            return target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            String msg = String.format("Unable to find %s field in %s class",
                    fieldName, target.getClass().getName());
            throw new TargetFieldNotFoundException(msg, e);
        }
    }

    /**
     * Returns the value of a field of an object.
     *
     * @param field  the field to retrieve the value from.
     * @param parent the object that contains the field.
     * @return the value of the field.
     * @throws TargetFieldNotAccessibleException if the field is inaccessible due to access restrictions.
     * @see Field#get(Object)
     */
    private Object getFieldValue(Field field, Object parent) {
        try {
            field.setAccessible(true);
            return field.get(parent);
        } catch (IllegalAccessException e) {
            String msg = String.format("Unable to access %s field in %s class",
                    field.getName(), parent.getClass().getName());
            throw new TargetFieldNotAccessibleException(msg, e);
        }
    }

    public static class Builder {
        private ObjectFactory objectFactory = new TargetObjectFactory();
        private FieldValueInsertionStrategy fieldValueInsertionStrategy = new SetterInsertionStrategy();
        private FieldExclusionStrategy fieldExclusionStrategy = new AnnotationExclusionStrategy();

        public Builder withObjectFactory(ObjectFactory objectFactory) {
            this.objectFactory = Objects.requireNonNull(objectFactory,
                    "Object initializer cannot be null.");
            return this;
        }

        public Builder withInsertionStrategy(FieldValueInsertionStrategy insertionStrategy) {
            this.fieldValueInsertionStrategy = Objects.requireNonNull(insertionStrategy,
                    "Field insertion strategy cannot be null.");
            ;
            return this;
        }

        public Builder withExclusionStrategy(FieldExclusionStrategy exclusionStrategy) {
            this.fieldExclusionStrategy = Objects.requireNonNull(exclusionStrategy,
                    "Field exclusion strategy cannot be null.");
            ;
            return this;
        }

        public ObjectMapper build() {
            return new ObjectMapperImpl(objectFactory, fieldValueInsertionStrategy, fieldExclusionStrategy);
        }

    }
}