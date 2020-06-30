package de.theo.json.schema.codegen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayType extends BaseType {

    private final boolean additionalItems;
    private List<BaseType> items;

    public ArrayType(String name, boolean additionalItems) {
        super(name);
        this.additionalItems = additionalItems;
        items = new ArrayList<>();
    }

    public List<BaseType> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(BaseType item) {
        items.add(item);
    }

    @Override
    public String toString() {
        return "ArrayType{" +
                "additionalItems=" + additionalItems +
                ", items=" + items +
                "} " + super.toString();
    }
}
