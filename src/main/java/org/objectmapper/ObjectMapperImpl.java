package org.objectmapper;

import org.objectmapper.exception.MappingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ObjectMapperImpl implements ObjectMapper {
    private boolean excludePrivateFields = false;

    @Override
    public <S, T> T mapObject(S source, Class<T> targetType, boolean excludePrivateFields) {
        this.excludePrivateFields = true;
        return mapObject(source, targetType);
    }

    @Override
    public <S, T> T mapObject(S source, Class<T> targetType) {
        if (Objects.isNull(source) || Objects.isNull(targetType)) {
            throw new IllegalArgumentException("Invalid parameters! Source object and target type cannot be null.");
        }
        try {
            return copySourceFieldsToTarget(source, targetType);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            String msg = String.format("Unable to map the objects due to: %s", e.getMessage());
            throw new MappingException(msg, e);
        }
    }

    private <S> Stream<Field> getSourceFields(S source) {
        return Stream.of(source.getClass().getDeclaredFields())
                .filter(predicateForFieldFiltering());
    }

    private Predicate<Field> predicateForFieldFiltering() {
        Predicate<Field> criteria = field -> !field.getName().equals(".class");
        if (excludePrivateFields) {
            criteria = criteria.and(field -> Modifier.isPrivate(field.getModifiers()));
        }
        return criteria;
    }

    private <S, T> T copySourceFieldsToTarget(S source, Class<T> targetType) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        T target = targetType.getDeclaredConstructor().newInstance();
        getSourceFields(source).forEach(sourceField -> insertValueIntoField(target, source, sourceField));
        return target;
    }

    private <T, S> void insertValueIntoField(T target, S source, Field sourceField) {
        sourceField.setAccessible(true);
        try {
            Field targetField = target.getClass().getField(sourceField.getName());
            targetField.setAccessible(true);
            targetField.set(target, sourceField.get(source));
        } catch (NoSuchFieldException e) {
            String msg = String.format("Cannot find %s field in target class.", sourceField.getName());
            throw new RuntimeException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = String.format("Cannot access %s field in target class.", sourceField.getName());
            throw new RuntimeException(msg, e);
        }
    }
}
