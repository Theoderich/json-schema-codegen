package de.theo.json.schema.codegen.model;

import java.util.List;

public class AllOf extends BaseType {

    private List<BaseType> types;

    public AllOf(String name, List<BaseType> types) {
        super(name);
        this.types = types;
    }

    @Override
    public String toString() {
        return "AllOf{" +
                "types=" + types +
                "} " + super.toString();
    }
}
