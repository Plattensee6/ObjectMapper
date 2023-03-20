package org.objectmapper.test.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.objectmapper.ObjectMapper;
import org.objectmapper.ObjectMapperImpl;
import org.objectmapper.annotation.ExcludeFromMapping;
import org.objectmapper.configuration.MappingConfiguration;
import org.objectmapper.exception.MappingException;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectMapperTest {
    private ObjectMapper testedMapper;
    private MappingConfiguration mockMappingConfiguration;

    @BeforeEach
    public void setUp() {
        // Default MappingConfiguration will be used.
        mockMappingConfiguration = mock(MappingConfiguration.class);
        testedMapper = new ObjectMapperImpl(mockMappingConfiguration);
    }

    @Test
    @DisplayName("Test ObjectMapperImpl with valid parameters")
    public void testMapObject() {
        TestSource source = new TestSource(1, "John", "Smith");
        ObjectMapper objectMapper = new ObjectMapperImpl();

        when(mockMappingConfiguration.getExcludedFieldsPredicate()).thenReturn(field -> true);

        TestTarget target = objectMapper.mapObject(source, TestTarget.class);
        assertNotNull(target);
        assertEquals(source.getPrivateId(), target.getPrivateId());
        assertEquals(source.getPrivateValue(), target.getPrivateValue());
        assertEquals(source.getProtectedValue(), target.getProtectedValue());
    }

    @Test
    @DisplayName("Test ObjectMapperImpl with null source parameter")
    public void testMapObjectWithNullSource() {
        String expectedMsg = "Invalid parameters! Source object and target type cannot be null.";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testedMapper.mapObject(null, TestTarget.class);
        });
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    @DisplayName("Test ObjectMapperImpl with null target type parameter")
    public void testMapObjectWithNullTargetType() {
        String expectedMsg = "Invalid parameters! Source object and target type cannot be null.";
        TestSource source = new TestSource(1, "John", "Smith");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testedMapper.mapObject(source, null);
        });
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    @DisplayName("Test ObjectMapperImpl with invalid target type parameter")
    public void testMapObjectWithInvalidTargetType() {
        TestSource source = new TestSource(1, "John", "");

        when(mockMappingConfiguration.getExcludedFieldsPredicate())
                .thenReturn(field -> true);

        assertThrows(MappingException.class, () -> {
            testedMapper.mapObject(source, Object.class);
        });
    }

    @Test
    @DisplayName("Test ObjectMapperImpl with excluded private modifier.")
    public void testMapObjectWithAllPrivateFieldExcluded() {
        TestSource source = new TestSource(1, "John", "ProtectedValueShouldBeMapped");

        // filter out all private field
        when(mockMappingConfiguration.getExcludedFieldsPredicate())
                .thenReturn(field -> !Modifier.isPrivate(field.getModifiers()));

        TestTarget target = testedMapper.mapObject(source, TestTarget.class);
        assertNotEquals(source.getPrivateId(), target.getPrivateId());
        assertNotEquals(source.getPrivateValue(), target.getPrivateValue());
        assertEquals(source.getProtectedValue(), target.getProtectedValue());
    }
    @Test
    @DisplayName("Test ObjectMapperImpl with ExcludeFromMapping annotation.")
    public void testMapObjectWithExcludedAnnotation(){
        TestSource source = new TestSource(1, "John", "Smith");

        // Fields annotated with ExcludeFromMapping should be filtered out.
        when(mockMappingConfiguration.getExcludedFieldsPredicate())
                .thenReturn(field -> !field.isAnnotationPresent(ExcludeFromMapping.class));

        TestTarget target = testedMapper.mapObject(source, TestTarget.class);
        assertEquals(source.getPrivateId(), target.getPrivateId());
        assertNull(target.getPrivateValue());
        assertNotEquals(source.getPrivateValue(), target.getPrivateValue());
    }
    public static class TestSource {
        private int privateId;
        @ExcludeFromMapping
        private String privateValue;
        protected String protectedValue;

        public TestSource() {
        }

        public TestSource(int id, String stringValue, String protectedValue) {
            this.privateId = id;
            this.privateValue = stringValue;
            this.protectedValue = protectedValue;
        }

        public int getPrivateId() {
            return privateId;
        }

        public String getPrivateValue() {
            return privateValue;
        }

        public String getProtectedValue() {
            return protectedValue;
        }

        @Override
        public String toString() {
            return "TestSource{" +
                    "id=" + privateId +
                    ", stringValue='" + privateValue + '\'' +
                    ", protectedValue='" + protectedValue + '\'' +
                    '}';
        }
    }

    public static class TestTarget {
        private int privateId;
        private String privateValue;
        protected String protectedValue;

        public TestTarget() {
        }

        public TestTarget(int id, String stringValue, String protectedValue) {
            this.privateId = id;
            this.privateValue = stringValue;
            this.protectedValue = protectedValue;
        }

        public int getPrivateId() {
            return privateId;
        }

        public String getPrivateValue() {
            return privateValue;
        }

        public String getProtectedValue() {
            return protectedValue;
        }

        @Override
        public String toString() {
            return "TestTarget{" +
                    "id=" + privateId +
                    ", stringValue='" + privateValue + '\'' +
                    ", protectedValue='" + protectedValue + '\'' +
                    '}';
        }
    }
}
