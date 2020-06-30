package de.theo.json.schema.codegen.model;

import java.util.Collections;
import java.util.List;

public class EnumType extends BaseType {

    private final List<String> enumValues;

    public EnumType(String title, List<String> enumValues) {
        super(title);
        this.enumValues = enumValues;
    }

    public List<String> getEnumValues() {
        return Collections.unmodifiableList(enumValues);
    }

    @Override
    public String toString() {
        return "EnumType{" +
                "enumValues=" + enumValues +
                "} " + super.toString();
    }
}
