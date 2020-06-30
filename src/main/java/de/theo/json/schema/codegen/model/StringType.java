package de.theo.json.schema.codegen.model;

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
