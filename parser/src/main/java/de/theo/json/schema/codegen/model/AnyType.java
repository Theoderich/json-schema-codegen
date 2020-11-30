package de.theo.json.schema.codegen.model;


import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

public class AnyType extends BaseType {

    public AnyType(String name) {
        super(name);
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.REFERENCE, getName(), optional);
    }

    @Override
    public String toString() {
        return "AnyType{} " + super.toString();
    }
}
