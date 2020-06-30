package de.theo.json.schema.codegen.model;

public class PatternType {

    private final String pattern;
    private final BaseType members;

    public PatternType(String pattern, BaseType members) {
        this.pattern = pattern;
        this.members = members;
    }

    public String getPattern() {
        return pattern;
    }


    @Override
    public String toString() {
        return "PatternType{" +
                "pattern='" + pattern + '\'' +
                ", members=" + members +
                '}';
    }
}
