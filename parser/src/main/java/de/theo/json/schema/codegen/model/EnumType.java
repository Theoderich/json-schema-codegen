package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumType extends BaseType implements ModelType {

    private final List<String> enumValues;

    public EnumType(String title, List<String> enumValues) {
        super(title);
        this.enumValues = enumValues;
    }

    public List<String> getEnumValues() {
        return Collections.unmodifiableList(enumValues);
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.REFERENCE, getName(), optional);
    }

    @Override
    public String toString() {
        return "EnumType{" +
                "enumValues=" + enumValues +
                "} " + super.toString();
    }

    @Override
    public ClassModel toClassModel() {
        return new ClassModel(getName(), new ArrayList<>(enumValues));
    }
}
