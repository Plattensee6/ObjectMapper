package org.objectmapper.test.model;

import org.objectmapper.annotation.ExcludeFromMapping;

public class SourceTestClass {
    private Integer id;
    private String name;
    @ExcludeFromMapping
    private String excludedField;
    private String fieldWithoutSetter;

    public SourceTestClass() {
    }

    public SourceTestClass(Integer id, String name, String excludedField) {
        this.id = id;
        this.name = name;
        this.excludedField = excludedField;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExcludedField() {
        return excludedField;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setExcludedField(String excludedField) {
        this.excludedField = excludedField;
    }

    public void setName(String name) {
        this.name = name;
    }
}
