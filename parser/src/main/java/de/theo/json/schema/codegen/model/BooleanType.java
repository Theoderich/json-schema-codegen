package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

public class BooleanType extends BaseType {
    public BooleanType(String name) {
        super(name);
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {

        return new PropertyModel(TypeModel.BOOLEAN, getName(), optional);
    }

    @Override
    public String toString() {
        return "BooleanType{} " + super.toString();
    }
}
