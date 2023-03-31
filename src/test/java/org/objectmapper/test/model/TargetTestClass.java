package org.objectmapper.test.model;

public class TargetTestClass {
    private Integer id;
    private String name;
    private String fieldWithoutSetter;
    private String excludedField;

    public TargetTestClass() {
    }

    public TargetTestClass(Integer id, String name, String fieldWithoutSetter) {
        this.id = id;
        this.name = name;
        this.fieldWithoutSetter = fieldWithoutSetter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExcludedField(String excludedField) {
        this.excludedField = excludedField;
    }
}
