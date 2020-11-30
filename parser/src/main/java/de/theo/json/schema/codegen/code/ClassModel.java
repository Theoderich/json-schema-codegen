package de.theo.json.schema.codegen.code;

import java.util.List;

public class ClassModel {

    private String name;
    private List<PropertyModel> properties;
    private List<PatternPropertyModel> patternProperties;
    private PropertyModel additionalProperties;
    private List<String> enumValues;

    public ClassModel(String name, List<String> enumValues) {
        this.name = name;
        this.enumValues = enumValues;
    }

    public ClassModel(String name, List<PropertyModel> properties, PropertyModel additionalProperties, List<PatternPropertyModel> patternProperties) {
        this.name = name;
        this.properties = properties;
        this.additionalProperties = additionalProperties;
    }

    public TypeModel getType() {
        return TypeModel.REFERENCE;
    }

    public String getName() {
        return name;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public boolean isHasAdditionalProperties() {
        return additionalProperties != null;
    }

    public boolean isHasPatternProperties() {
        return patternProperties != null && !patternProperties.isEmpty();
    }

    public PropertyModel getAdditionalProperties() {
        return additionalProperties;
    }

    public List<PatternPropertyModel> getPatternProperties() {
        return patternProperties;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public boolean isEnum() {
        return enumValues != null;
    }
}
