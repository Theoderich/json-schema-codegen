package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

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
    public TypeName toTypeName(String targetPackage) {
        return ClassName.get(String.class);
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
    public String toString() {
        return "StringType{" +
                "minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", pattern='" + pattern + '\'' +
                "} " + super.toString();
    }
}
