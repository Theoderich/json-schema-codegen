package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

public class IntegerType extends NumberType {
    public IntegerType(String name, Integer multipleOf, Integer minimum, Integer exclusiveMinimum, Integer maximum, Integer exclusiveMaximum) {
        super(name, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.INTEGER, getName(), optional);
    }

    @Override
    public String toString() {
        return "IntegerType{} " + super.toString();
    }
}
