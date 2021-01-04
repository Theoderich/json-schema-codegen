package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.TypeModel;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotType extends BaseType {

    private BaseType type;

    public NotType(String name, BaseType type) {
        super(name);
        this.type = type;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        type.resolveReferences(refMap);
    }

    @Override
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(TypeModel.REFERENCE, getName(), optional);
    }

    @Override
    public List<BaseType> children() {
        return Collections.singletonList(type);
    }

    @Override
    public String toString() {
        return "OneOf{" +
                "type=" + type +
                "} " + super.toString();
    }
}
