package de.theo.json.schema.codegen.model;

public class IntegerType extends NumberType {
    public IntegerType(String name, Integer multipleOf, Integer minimum, Integer exclusiveMinimum, Integer maximum, Integer exclusiveMaximum) {
        super(name, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
    }

    @Override
    public String toString() {
        return "IntegerType{} " + super.toString();
    }
}
