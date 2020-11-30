package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllOf extends BaseType {

    private List<BaseType> types;

    public AllOf(String name, List<BaseType> types) {
        super(name);
        this.types = types;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        for (BaseType type : this.types) {
            type.resolveReferences(refMap);
        }
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.REFERENCE, getName(), optional);
    }

    @Override
    public List<BaseType> children() {
        return new ArrayList<>(types);
    }

    @Override
    public String toString() {
        return "AllOf{" +
                "types=" + types +
                "} " + super.toString();
    }
}
