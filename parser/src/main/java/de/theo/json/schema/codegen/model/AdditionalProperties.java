package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class AdditionalProperties {

    private final boolean allowed;
    private final BaseType definition;

    public AdditionalProperties(boolean allowed, BaseType definition) {
        this.allowed = allowed;
        this.definition = definition;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public BaseType getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "AdditionalProperties{" +
                "allowed=" + allowed +
                ", definition=" + definition +
                '}';
    }

    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        this.definition.resolveReferences(refMap);
    }
}
