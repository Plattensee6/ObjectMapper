package org.objectmapper;

import org.objectmapper.configuration.DefaultMappingConfiguration;
import org.objectmapper.configuration.MappingConfiguration;
import org.objectmapper.exception.MappingException;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ObjectMapperImpl implements ObjectMapper {
    private final MappingConfiguration mappingConfiguration;

    public ObjectMapperImpl(MappingConfiguration mappingConfig) {
        this.mappingConfiguration = Optional.ofNullable(mappingConfig).orElse(new DefaultMappingConfiguration());
    }

    @Override
    public <S, T> T mapObject(S source, Class<T> targetType) {
        if (Objects.isNull(source) || Objects.isNull(targetType)) {
            throw new IllegalArgumentException("Invalid parameters! Source object and target type cannot be null.");
        }
        try {
            return copySourceFieldsToTarget(source, targetType);
        } catch (ReflectiveOperationException e) {
            String msg = String.format("Unable to map the objects due to: %s", e.getMessage());
            throw new MappingException(msg, e);
        }
    }

    private <S> Stream<Field> getSourceFields(S source) {
        return Stream.of(source.getClass().getDeclaredFields())
                .filter(mappingConfiguration.getExcludedFieldPredicate());
    }

    private <S, T> T copySourceFieldsToTarget(S source, Class<T> targetType) throws ReflectiveOperationException {
        T target = targetType.getDeclaredConstructor().newInstance();
        getSourceFields(source).forEach(field -> {
            try {
                insertValueIntoField(target, source, field);
            } catch (ReflectiveOperationException e) {
                throw new MappingException("Unable to copy source field value into target object.", e);
            }
        });
        return target;
    }

    private <T, S> void insertValueIntoField(T target, S source, Field sourceField) throws ReflectiveOperationException {
        sourceField.setAccessible(true);
        Field targetField = target.getClass().getField(sourceField.getName());
        targetField.setAccessible(true);
        targetField.set(target, sourceField.get(source));
    }
}
