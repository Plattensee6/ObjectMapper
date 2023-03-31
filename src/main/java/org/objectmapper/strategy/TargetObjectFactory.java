package org.objectmapper.strategy;

import org.objectmapper.exception.TargetObjectInstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 A factory implementation that creates objects of the given class type by invoking its no-arg constructor using reflection.
 If the target class does not have a no-arg constructor, an exception will be thrown.
 */
public class TargetObjectFactory implements ObjectFactory {
    /**
     * Creates a new instance of the given class type using its no-arg constructor.
     *
     * @param type the class type to instantiate
     * @return a new instance of the given class type
     * @throws TargetObjectInstantiationException if the target class cannot be instantiated
     */
    @Override
    public <T> T create(Class<T> type) {
        if (Objects.isNull(type)){
            throw new IllegalArgumentException("Class type parameter in create method cannot be null.");
        }
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InvocationTargetException e) {
            String msg = String.format("Unable to invoke %s constructor.", type.getName());
            throw new TargetObjectInstantiationException(msg, e.getTargetException());
        } catch (InstantiationException e) {
            String msg = String.format("Unable to instantiate target class: %s", type.getName());
            throw new TargetObjectInstantiationException(msg, e);
        } catch (NoSuchMethodException e) {
            String msg = String.format(
                    "Unable to find %s class constructor. No-arg constructor is needed",
                    type.getName());
            throw new TargetObjectInstantiationException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = String.format("Unable to access %s class constructor.", type.getName());
            throw new TargetObjectInstantiationException(msg, e);
        }
    }
}
