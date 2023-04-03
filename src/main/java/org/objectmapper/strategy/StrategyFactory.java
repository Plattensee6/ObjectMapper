package org.objectmapper.strategy;

import java.util.Objects;

public class StrategyFactory {
    private static StrategyFactory instance;

    private StrategyFactory() {
    }

    public FieldExclusionStrategy getDefaultFieldExclusionStrategy(){
        return new AnnotationExclusionStrategy();
    }
    public FieldValueInsertionStrategy getDefaultFieldInsertionStrategy(){
        return new SetterInsertionStrategy();
    }
    public ObjectFactory getDefaultObjectFactory(){
        return new TargetObjectFactory();
    }
    public static StrategyFactory getInstance() {
        if (Objects.isNull(instance)) {
            return new StrategyFactory();
        }
        return instance;
    }
}
