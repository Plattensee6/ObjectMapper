package org.objectmapper.strategy;

import org.objectmapper.annotation.ExcludeFromMapping;
import org.objectmapper.exception.TargetFieldNotAccessibleException;
import org.objectmapper.exception.TargetSetterMethodNotFound;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The SetterInsertionStrategy class implements the {@link FieldValueInsertionStrategy} interface, which defines a method
 * for inserting a value into a field of a target object using the corresponding setter method.
 * The strategy implemented in this class assumes that the target object has a setter method for the field being set, and
 * throws exceptions if it cannot access the field or the setter method.
 */
public class SetterInsertionStrategy implements FieldValueInsertionStrategy {
    /**
     * Inserts the source value into the target object's field using the corresponding setter method.
     *
     * @param sourceValue  the value to insert into the target object's field
     * @param targetObject the target object into which the source value will be inserted
     * @param targetField  the field in the target object into which the source value will be inserted
     * @throws TargetFieldNotAccessibleException if the target field cannot be accessed
     * @throws TargetSetterMethodNotFound        if the target field's setter method cannot be found or invoked
     */
    @Override
    public void insertValue(Object sourceValue, Object targetObject, Field targetField) {
        checkForAnnotation(targetField);
        try {
            Method setter = findSetterMethod(targetObject, targetField, sourceValue.getClass());
            setter.setAccessible(true);
            setter.invoke(targetObject, sourceValue);
        } catch (IllegalAccessException e) {
            String msg = String.format("Cannot access %s field in %s.", targetField.getName(), targetObject.getClass().getName());
            throw new TargetFieldNotAccessibleException(msg, e);
        } catch (InvocationTargetException e) {
            String msg = String.format("Failed to invoke %s field's setter method in %s ",
                    targetField.getName(), targetObject.getClass().getName());
            throw new TargetFieldNotAccessibleException(msg, e);
        } catch (NoSuchMethodException e) {
            String msg = String.format("%s field's setter method not found in %s ",
                    targetField.getName(), targetObject.getClass().getName());
            throw new TargetSetterMethodNotFound(msg, e);
        }
    }

    private void checkForAnnotation(Field field) {
        if (field.isAnnotationPresent(ExcludeFromMapping.class)) {
            throw new TargetFieldNotAccessibleException(field.getName()
                    + "field is not accessible, because annotated with @ExcludeFromMapping annotation");
        }
    }

    private Method findSetterMethod(Object targetObject,
                                    Field targetField,
                                    Class<?> sourceValueType) throws NoSuchMethodException {
        String setterName = getSetterMethodName(targetField);
        return targetObject.getClass().getMethod(setterName, sourceValueType);
    }

    private String getSetterMethodName(Field field) {
        String camelCaseLetter = field.getName().substring(0, 1).toUpperCase();
        return "set" + camelCaseLetter + field.getName().substring(1);
    }
}
