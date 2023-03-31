package org.objectmapper.test.strategy;

import org.junit.Before;
import org.junit.Test;
import org.objectmapper.exception.TargetObjectInstantiationException;
import org.objectmapper.strategy.ObjectFactory;
import org.objectmapper.strategy.TargetObjectFactory;
import org.objectmapper.test.model.TargetTestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TargetObjectFactoryTest {
    private ObjectFactory objectFactory;

    @Before
    public void setUp(){
        this.objectFactory = new TargetObjectFactory();
    }
    @Test
    public void testCreateWithValidType(){
        TargetTestClass createdObject = objectFactory.create(TargetTestClass.class);
        assertNotNull(createdObject);
    }
    @Test
    public void testCreateNoArgConstructorNotFound() {
        assertThrows(TargetObjectInstantiationException.class,
                () -> objectFactory.create(TestClassWithoutNoArgsConstructor.class));
    }
    @Test
    public void testCreateWithNullType(){
        assertThrows(IllegalArgumentException.class, () -> objectFactory.create(null));
    }

    @Test
    public void testCreateWithPrivateNoArgConstructor(){
        assertThrows(TargetObjectInstantiationException.class,
                () -> objectFactory.create(TestClassWithPrivateNoArgConstructor.class));
    }
    @Test
    public void testCreateWithNotAccessibleConstructor() {
        mockConstructorAndThrow(IllegalAccessException.class);
        assertThrows(TargetObjectInstantiationException.class,
                () -> objectFactory.create(TestClassWithPrivateNoArgConstructor.class));
    }
    @Test
    public void testCreateUnableToInstantiate() {
        mockConstructorAndThrow(InstantiationException.class);
        assertThrows(TargetObjectInstantiationException.class,
                () -> objectFactory.create(TestClassWithPrivateNoArgConstructor.class));
    }

    private void mockConstructorAndThrow(Class<? extends Exception> exception){
        Constructor constructor = mock(Constructor.class);
        try {
            when(constructor.newInstance()).thenCallRealMethod().thenThrow(exception);
        } catch (InstantiationException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Stubbing Constructor newInstance method has failed in test case.", e);
        }
    }
    private static class TestClassWithoutNoArgsConstructor {
        private int field1;

        public TestClassWithoutNoArgsConstructor(int field1) {
            this.field1 = field1;
        }
    }
    private static class TestClassWithPrivateNoArgConstructor {
        private int id;

        private TestClassWithPrivateNoArgConstructor(int id) {
            this.id = id;
        }
    }
}
