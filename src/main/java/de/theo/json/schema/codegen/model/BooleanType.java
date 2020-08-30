package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.TypeName;

public class BooleanType extends BaseType {
    public BooleanType(String name) {
        super(name);
    }

    @Override
    public TypeName toTypeName(String targetPackage) {
        return TypeName.BOOLEAN;
    }

    @Override
    public String toString() {
        return "BooleanType{} " + super.toString();
    }
}
