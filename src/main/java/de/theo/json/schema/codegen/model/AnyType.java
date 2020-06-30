package de.theo.json.schema.codegen.model;


public class AnyType extends BaseType {

    public AnyType(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "AnyType{} " + super.toString();
    }
}
