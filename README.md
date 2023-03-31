#ObjectMapper

The ObjectMapper is a utility class that maps fields from a source object to a target object using Java reflection. This implementation provides default strategies for handling excluded fields and field value insertion.
Features

    Maps fields from a source object to a target object using Java reflection
    Provides default strategies for handling excluded fields and field value insertion
    Supports mapping fields of different types as long as they have the same fields

##Usage

To use the ObjectMapper, first create an instance of the class with the desired mapping configuration:

```java
ObjectMapperImpl.Builder()
                .withInsertionStrategy(fieldValueInsertionStrategy)
                .withObjectFactory(objectFactory)
                .withExclusionStrategy(fieldExclusionStrategy)
                .build();
```


Once you have an ObjectMapper, you can use the mapObject method to map fields from a source object to a target object:


    objectMapper.mapObject(sourceObject, targetType);

Here, sourceObject is the object from which to copy the fields, targetType is the type of the target object, and targetObject is the new target object with the copied fields.
##Configuration

###Object Factory
The ObjectFactory interface is used to create new instances of the target object. The ObjectMapperImpl constructor takes an ObjectFactory parameter that is used to create new instances of the target object. You can create your own implementation of the ObjectFactory interface and pass it to the ObjectMapperImpl constructor if you need custom object instantiation behavior.

###Field Value Insertion Strategy
The FieldValueInsertionStrategy interface is used to insert values into the target object fields. The ObjectMapperImpl constructor takes a FieldValueInsertionStrategy parameter that is used to insert values into the target object fields. You can create your own implementation of the FieldValueInsertionStrategy interface and pass it to the ObjectMapperImpl constructor if you need custom field value insertion behavior.

###Field Exclusion Strategy
The FieldExclusionStrategy interface is used to determine which fields to exclude from the mapping (**from the source class**). The ObjectMapperImpl constructor takes a FieldExclusionStrategy parameter that is used to determine which fields to exclude from the mapping. You can create your own implementation of the FieldExclusionStrategy interface and pass it to the ObjectMapperImpl constructor if you need custom field exclusion behavior.

###Exceptions
The following exceptions may be thrown by the ObjectMapper:

    IllegalArgumentException: if either source or targetType is null.
    TargetFieldNotFoundException: if the target field cannot be found.
    TargetFieldNotAccessibleException: if the target field cannot be accessed.
	TargetObjectInstantiationException: if the target class cannot be instantiated. 
	TargetSetterMethodNotFoundException: if the setter doesn't not exists. You should use wrapper classes for setter method parameter.

###License
The ObjectMapper is released under the MIT License.
