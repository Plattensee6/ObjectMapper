package org.objectmapper.strategy;

/**
 An interface for creating instances of a given class.
 */
public interface ObjectFactory {
    /**

     Creates a new instance of the specified type.
     @param type the class of the object to create
     @param <T> the type of the object to create
     @return a new instance of the specified type
     */
    <T>T create(Class<T> type);
}
