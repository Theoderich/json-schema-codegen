package de.theo.json.schema.codegen.model;

public class BaseType {

    private final String name;

    public BaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BaseType{" +
                "name='" + name + '\'' +
                '}';
    }
}
