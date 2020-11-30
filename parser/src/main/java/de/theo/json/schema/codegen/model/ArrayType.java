package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArrayType extends BaseType {

    private final boolean additionalItems;
    private List<BaseType> items;

    public ArrayType(String name, boolean additionalItems) {
        super(name);
        this.additionalItems = additionalItems;
        items = new ArrayList<>();
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        for (BaseType item : this.items) {
            item.resolveReferences(refMap);
        }
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        if (additionalItems || items.size() > 1) {
            return new PropertyModel(TypeModel.REFERENCE, getName(), optional, true);
        }
        return new PropertyModel(items.get(0).toPropertyModel(optional).getType(), getName(), optional, true);

    }

    @Override
    public List<BaseType> children() {
        return new ArrayList<>(items);
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
