package de.theo.json.schema.codegen.model;

import java.util.List;

public class OneOf extends BaseType {

    private List<BaseType> types;

    public OneOf(String name, List<BaseType> types) {
        super(name);
        this.types = types;
    }

    @Override
    public String toString() {
        return "OneOf{" +
                "types=" + types +
                "} " + super.toString();
    }
}
