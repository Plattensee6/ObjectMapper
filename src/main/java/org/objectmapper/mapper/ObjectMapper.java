package org.objectmapper.mapper;

/**
 * An interface representing an object mapper that can map objects from one type to another.
 */
public interface ObjectMapper {
    /**
     * Maps an object of type S to an object of type T.
     *
     * @param source     the object to map from
     * @param targetType the class of the target object to map to
     * @param <S>        the type of the source object
     * @param <T>        the type of the target object
     * @return the mapped target object
     */
    <S, T> T mapObject(S source, Class<T> targetType);

}
