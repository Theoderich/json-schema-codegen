package de.theo.json.schema.codegen.model;


import com.squareup.javapoet.TypeName;

public class AnyType extends BaseType {

    public AnyType(String name) {
        super(name);
    }

    @Override
    public TypeName toTypeName(String targetPackage) {
        return TypeName.OBJECT;
    }

    @Override
    public String toString() {
        return "AnyType{} " + super.toString();
    }
}
