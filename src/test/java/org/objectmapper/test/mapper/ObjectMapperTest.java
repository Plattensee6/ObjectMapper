package org.objectmapper.test.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectmapper.exception.TargetObjectInstantiationException;
import org.objectmapper.mapper.ObjectMapper;
import org.objectmapper.mapper.ObjectMapperImpl;
import org.objectmapper.strategy.AnnotationExclusionStrategy;
import org.objectmapper.strategy.FieldExclusionStrategy;
import org.objectmapper.strategy.FieldValueInsertionStrategy;
import org.objectmapper.strategy.ObjectFactory;
import org.objectmapper.strategy.SetterInsertionStrategy;
import org.objectmapper.strategy.TargetObjectFactory;
import org.objectmapper.test.model.SourceTestClass;
import org.objectmapper.test.model.TargetTestClass;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ObjectMapperTest {

    private ObjectFactory objectFactory;
    private FieldValueInsertionStrategy fieldValueInsertionStrategy;
    private FieldExclusionStrategy fieldExclusionStrategy;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectFactory = mock(TargetObjectFactory.class);
        fieldValueInsertionStrategy = mock(SetterInsertionStrategy.class);
        fieldExclusionStrategy = mock(AnnotationExclusionStrategy.class);
        objectMapper = new ObjectMapperImpl.Builder()
                .withInsertionStrategy(fieldValueInsertionStrategy)
                .withObjectFactory(objectFactory)
                .withExclusionStrategy(fieldExclusionStrategy)
                .build();
    }

    @Test
    void mapObject_givenNullSource_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> objectMapper.mapObject(null, TargetTestClass.class));
    }

    @Test
    void mapObject_givenNullTargetType_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> objectMapper.mapObject("Test", null));
    }

    @Test
    void mapObject_givenValidSourceAndTargetType_shouldCallCopySourceFieldsToTarget() {
        // Arrange
        SourceTestClass source = new SourceTestClass(1, "Test1", "");
        Class<TargetTestClass> targetType = TargetTestClass.class;
        when(objectFactory.create(targetType)).thenReturn(new TargetTestClass());
        when(fieldExclusionStrategy.filter(any())).thenReturn(getSourceFields());

        // Act
        objectMapper.mapObject(source, TargetTestClass.class);

        // Assert
        verify(objectFactory, times(1)).create(targetType);
        verify(fieldExclusionStrategy, times(1)).filter(any());
        verify(fieldValueInsertionStrategy, times(3)).insertValue(any(Object.class), any(Object.class), any(Field.class));
    }

    @Test
    void mapObject_givenInvalidTargetType_shouldThrowTargetObjectInstantiationException() {
        // Arrange
        SourceTestClass source = new SourceTestClass(1, "Test1", "");
        when(objectFactory.create(any())).thenThrow(TargetObjectInstantiationException.class);

        assertThrows(TargetObjectInstantiationException.class, () -> objectMapper.mapObject(source, String.class));
    }

    @Test
    public void test() {
        SourceTestClass source = new SourceTestClass(1, "Test1", "");
        var target = objectMapper.mapObject(source, ObjectMapperImpl.class);
        System.out.println(target);
    }

    private Stream<Field> getSourceFields() {
        return Stream.of(SourceTestClass.class.getDeclaredFields());
    }
}
