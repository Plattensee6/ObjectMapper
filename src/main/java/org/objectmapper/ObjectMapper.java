package org.objectmapper;

public interface ObjectMapper {
    <S, T> T mapObject(S source, Class<T> targetType);

}
