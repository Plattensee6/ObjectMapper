package org.objectmapper;

import org.objectmapper.configuration.DefaultMappingConfiguration;
import org.objectmapper.configuration.MappingConfiguration;
import org.objectmapper.exception.TargetFieldNotAccessibleException;
import org.objectmapper.exception.TargetFieldNotFoundException;
import org.objectmapper.exception.TargetObjectInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.stream.Stream;

public class ObjectMapperImpl implements ObjectMapper {
    private final MappingConfiguration mappingConfiguration;

    public ObjectMapperImpl(MappingConfiguration mappingConfig) {
        if (Objects.isNull(mappingConfig)) {
            throw new IllegalArgumentException("Mapping configuration cannot be null. " +
                    "Use the no-arg constructor or provide a valid MappingConfiguration.");
        }
        this.mappingConfiguration = mappingConfig;
    }

    public ObjectMapperImpl() {
        mappingConfiguration = new DefaultMappingConfiguration();
    }

    @Override
    public <S, T> T mapObject(S source, Class<T> targetType) {
        if (Objects.isNull(source) || Objects.isNull(targetType)) {
            throw new IllegalArgumentException("Invalid parameters! Source object and target type cannot be null.");
        }
        return copySourceFieldsToTarget(source, targetType);
    }

    private <S> Stream<Field> getSourceFields(S source) {
        return Stream.of(source.getClass().getDeclaredFields())
                .filter(mappingConfiguration.getExcludedFieldsPredicate());
    }

    private <S, T> T copySourceFieldsToTarget(S source, Class<T> targetType) {
        T target = instantiateTargetObject(targetType);
        getSourceFields(source).forEach(field -> insertValueIntoField(target, source, field));
        return target;
    }

    private <T> T instantiateTargetObject(Class<T> targetType) {
        try {
            return targetType.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException e) {
            String msg = String.format("Unable to invoke %s constructor.", targetType.getName());
            throw new TargetObjectInstantiationException(msg, e.getTargetException());
        } catch (InstantiationException e) {
            String msg = String.format("Unable to instantiate target class: %s", targetType.getName());
            throw new TargetObjectInstantiationException(msg, e);
        } catch (NoSuchMethodException e) {
            String msg = String.format(
                    "Unable to find %s class constructor. No-arg constructor is needed",
                    targetType.getName());
            throw new TargetObjectInstantiationException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = String.format("Unable to access %s class constructor.", targetType.getName());
            throw new TargetObjectInstantiationException(msg, e);
        }
    }

    private <T, S> void insertValueIntoField(T target, S source, Field sourceField) {
        try {
            sourceField.setAccessible(true);
            Field targetField = target.getClass().getDeclaredField(sourceField.getName());
            targetField.setAccessible(true);
            targetField.set(target, sourceField.get(source));
        } catch (NoSuchFieldException e) {
            String msg = String.format("Cannot find %s field in %s.", sourceField.getName(), target.getClass().getName());
            throw new TargetFieldNotFoundException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = String.format("Cannot access %s field in %s.", sourceField.getName(), target.getClass().getName());
            throw new TargetFieldNotAccessibleException(msg, e);
        }
    }
}
