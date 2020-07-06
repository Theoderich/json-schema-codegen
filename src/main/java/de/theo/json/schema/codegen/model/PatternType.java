package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class PatternType {

    private final String pattern;
    private final BaseType members;

    public PatternType(String pattern, BaseType members) {
        this.pattern = pattern;
        this.members = members;
    }

    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        this.members.resolveReferences(refMap);
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
