package org.objectmapper.test.strategy;

import org.junit.Before;
import org.junit.Test;
import org.objectmapper.annotation.ExcludeFromMapping;
import org.objectmapper.strategy.AnnotationExclusionStrategy;
import org.objectmapper.strategy.FieldExclusionStrategy;
import org.objectmapper.test.model.SourceTestClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AnnotationExclusionStrategyTest {
    private FieldExclusionStrategy fieldExclusionStrategy;
    @Before
    public void setUp(){
        fieldExclusionStrategy = new AnnotationExclusionStrategy();
    }
    @Test
    public void testFilterWithNullFieldsParameter(){
        assertThrows(IllegalArgumentException.class, () -> fieldExclusionStrategy.filter(null));
    }
    @Test
    public void testFilterWithOneFieldExcluded() {
        SourceTestClass testObject = new SourceTestClass();
        Stream<Field> declaredFields = Arrays.stream(testObject.getClass().getDeclaredFields());
        List<Field> filteredFields = fieldExclusionStrategy.filter(declaredFields).toList();

        assertTrue(filteredFields.stream().noneMatch(field -> field.isAnnotationPresent(ExcludeFromMapping.class)));
        // SourceTestClass has 4 fields, one of the has ExcludeFromMapping annotation.
        assertEquals(3, filteredFields.size());
    }
}
