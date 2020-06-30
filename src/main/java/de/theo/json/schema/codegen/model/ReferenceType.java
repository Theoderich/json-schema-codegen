package de.theo.json.schema.codegen.model;

public class ReferenceType extends BaseType {

    private final String reference;

    public ReferenceType(String name, String reference) {
        super(name);
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "ReferenceType{" +
                "reference='" + reference + '\'' +
                "} " + super.toString();
    }
}
