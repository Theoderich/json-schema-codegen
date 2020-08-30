package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.TypeName;

public class NullType extends BaseType {

    public NullType() {
        super("");
    }

    @Override
    public TypeName toTypeName(String targetPackage) {
        return TypeName.VOID;
    }

    @Override
    public String toString() {
        return "NullType{} " + super.toString();
    }
}
