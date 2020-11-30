package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

public class NullType extends BaseType {

    public NullType() {
        super("");
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.NULL, getName(), optional);
    }

    @Override
    public String toString() {
        return "NullType{} " + super.toString();
    }
}
