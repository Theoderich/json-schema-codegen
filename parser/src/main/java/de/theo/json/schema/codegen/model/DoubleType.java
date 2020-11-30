package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.TypeName;

public class DoubleType extends NumberType {
    public DoubleType(String name, Integer multipleOf, Integer minimum, Integer exclusiveMinimum, Integer maximum, Integer exclusiveMaximum) {
        super(name, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
    }


    @Override
    public TypeName toTypeName(String targetPackage) {
        return TypeName.DOUBLE;
    }

    @Override
    public String toString() {
        return "DoubleType{} " + super.toString();
    }
}
