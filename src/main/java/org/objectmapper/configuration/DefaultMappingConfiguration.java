package org.objectmapper.configuration;

import org.objectmapper.annotation.ExcludeFromMapping;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

public class DefaultMappingConfiguration implements MappingConfiguration {
    private Set<Integer> excludedModifiers;

    public DefaultMappingConfiguration() {
    }

    private DefaultMappingConfiguration(Set<Integer> excludedModifiers) {
        this.excludedModifiers = excludedModifiers;
    }

    @Override
    public Predicate<Field> getExcludedFieldPredicate() {
        Predicate<Field> excludeFieldPredicate =  field -> !field.getName().equals("serialVersionUID")
                && !field.isSynthetic()
                && !field.getName().startsWith("$");

        Predicate<Field> excludeAnnotationAbsent = field -> !field.isAnnotationPresent(ExcludeFromMapping.class);
        excludeFieldPredicate = excludeFieldPredicate.and(excludeAnnotationAbsent);
        if (excludedModifiers != null && !excludedModifiers.isEmpty()){
            Predicate<Field> excludeFieldByModifier = (field) -> !excludedModifiers.contains(field.getModifiers());
            excludeFieldPredicate = excludeFieldPredicate.and(excludeFieldByModifier);
        }
        return excludeFieldPredicate;
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
