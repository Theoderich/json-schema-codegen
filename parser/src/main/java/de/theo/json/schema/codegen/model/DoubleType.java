package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

public class DoubleType extends NumberType {
    public DoubleType(String name, Integer multipleOf, Integer minimum, Integer exclusiveMinimum, Integer maximum, Integer exclusiveMaximum) {
        super(name, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
    }


    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.FLOAT, getName(), optional);
    }

    @Override
    public String toString() {
        return "DoubleType{} " + super.toString();
    }
}
