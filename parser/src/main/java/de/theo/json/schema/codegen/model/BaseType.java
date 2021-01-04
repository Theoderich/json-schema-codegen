package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseType {

    private final String name;

    public BaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<BaseType> children(){
        return Collections.emptyList();
    }

    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {

    }

    public abstract PropertyModel toPropertyModel(boolean optional);


    @Override
    public String toString() {
        return "name='" + name + "'\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseType baseType = (BaseType) o;
        return Objects.equals(name, baseType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
