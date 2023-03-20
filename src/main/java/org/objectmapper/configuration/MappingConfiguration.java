package org.objectmapper.configuration;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public interface MappingConfiguration {
    Predicate<Field> getExcludedFieldsPredicate();

}
