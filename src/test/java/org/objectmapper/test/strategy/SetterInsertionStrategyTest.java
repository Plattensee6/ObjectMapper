package org.objectmapper.test.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectmapper.exception.TargetSetterMethodNotFound;
import org.objectmapper.strategy.SetterInsertionStrategy;
import org.objectmapper.test.model.TargetTestClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetterInsertionStrategyTest {
    private SetterInsertionStrategy setterInsertionStrategy;

    @BeforeEach
    public void setUp() {
        setterInsertionStrategy = new SetterInsertionStrategy();
    }

    @Test
    public void testInsertValue() throws NoSuchFieldException {
        // Arrange
        Object sourceValue = "foo";
        TargetTestClass targetObject = new TargetTestClass();
        Field targetField = targetObject.getClass().getDeclaredField("name");
        // Act
        setterInsertionStrategy.insertValue(sourceValue, targetObject, targetField);
        // Assert
        assertEquals(sourceValue, targetObject.getName());
    }

    @Test
    public void testInsertValueWhenTargetSetterMethodNotFound() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String sourceValue = "foo";
        TargetTestClass targetObject = new TargetTestClass();
        Field targetField = targetObject.getClass().getDeclaredField("fieldWithoutSetter");

        Method targetSetter = mock(Method.class);
        when(targetSetter.invoke(any(), any())).thenCallRealMethod().thenThrow(TargetSetterMethodNotFound.class);

        // Act & Assert
        assertThrows(TargetSetterMethodNotFound.class,
                () -> setterInsertionStrategy.insertValue(sourceValue, targetObject, targetField));
    }
}
