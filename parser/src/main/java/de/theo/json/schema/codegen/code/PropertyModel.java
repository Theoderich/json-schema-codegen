package de.theo.json.schema.codegen.code;

public class PropertyModel {

    private TypeModel type;
    private String name;
    private boolean optional;
    private boolean array;
    private ReferenceModel reference;


    public PropertyModel(String name, boolean optional, boolean array, ReferenceModel reference) {
        this(TypeModel.REFERENCE, name, optional, array, reference);
    }

    public PropertyModel(TypeModel type, String name, boolean optional) {
        this(type, name, optional, false);
    }

    public PropertyModel(TypeModel type, String name, boolean optional, boolean array) {
        this(type, name, optional, array, null);
    }

    private PropertyModel(TypeModel type, String name, boolean optional, boolean array, ReferenceModel reference) {
        this.type = type;
        this.name = name;
        this.optional = optional;
        this.array = array;
        this.reference = reference;
    }

    public TypeModel getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isHasReference() {
        return reference != null;
    }

    public ReferenceModel getReference() {
        return reference;
    }

}
