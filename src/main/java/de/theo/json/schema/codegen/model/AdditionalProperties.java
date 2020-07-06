package de.theo.json.schema.codegen.model;

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
}
