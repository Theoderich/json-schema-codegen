package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

import java.util.Objects;

public class StringType extends BaseType {

    private Integer minLength;
    private Integer maxLength;
    private String pattern;

    public StringType(String name) {
        super(name);
    }

    public StringType(String name, Integer minLength, Integer maxLength, String pattern) {
        super(name);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.STRING, getName(), optional);
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public String getPattern() {
        return pattern;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StringType that = (StringType) o;
        return Objects.equals(minLength, that.minLength) && Objects.equals(maxLength, that.maxLength) && Objects.equals(pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minLength, maxLength, pattern);
    }

    @Override
    public String toString() {
        return "StringType{" +
                "minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", pattern='" + pattern + '\'' +
                "} " + super.toString();
    }
}
