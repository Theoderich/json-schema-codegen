package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public abstract class BaseType {

    private final String name;

    public BaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {

    }

    @Override
    public String toString() {
        return "name='" + name + "'\n";
    }
}
