package de.theo.json.schema.codegen.model;

public class NullType extends BaseType {
    public NullType() {
        super("");
    }

    @Override
    public String toString() {
        return "NullType{} " + super.toString();
    }
}
