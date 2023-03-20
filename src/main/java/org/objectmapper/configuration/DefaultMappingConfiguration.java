package org.objectmapper.configuration;

import org.objectmapper.annotation.ExcludeFromMapping;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class DefaultMappingConfiguration implements MappingConfiguration {
    private Set<Integer> excludedModifiers;

    public DefaultMappingConfiguration() {
    }

    private DefaultMappingConfiguration(Set<Integer> excludedModifiers) {
        if (Objects.isNull(excludedModifiers)) {
            throw new IllegalArgumentException("The set of excluded modifier cannot be null.");
        }
        this.excludedModifiers = excludedModifiers;
    }

    @Override
    public Predicate<Field> getExcludedFieldsPredicate() {
        Predicate<Field> excludeFieldsPredicate = excludeFieldNamePredicate()
                .and(annotationAbsentPredicate());
        if (excludedModifiers != null && !excludedModifiers.isEmpty()) {
            excludeFieldsPredicate = excludeFieldsPredicate.and(excludeFieldByModifierPredicate());
        }
        return excludeFieldsPredicate;
    }

    private Predicate<Field> excludeFieldNamePredicate() {
        return field -> !field.getName().equals("serialVersionUID")
                && !field.isSynthetic()
                && !field.getName().startsWith("$");
    }

    private Predicate<Field> annotationAbsentPredicate() {
        return field -> !field.isAnnotationPresent(ExcludeFromMapping.class);
    }

    private Predicate<Field> excludeFieldByModifierPredicate() {
        return (field) -> !excludedModifiers.contains(field.getModifiers());
    }

    public static class ConfigBuilder {
        private Set<Integer> excludedModifiers;

        public ConfigBuilder withExcludedModifiers(Integer... modifiers) {
            this.excludedModifiers = Set.of(modifiers);
            return this;
        }

        public DefaultMappingConfiguration build() {
            return new DefaultMappingConfiguration(excludedModifiers);
        }
    }
}
