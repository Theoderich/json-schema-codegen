package de.theo.json.schema.codegen.code;

public class PatternPropertyModel extends PropertyModel {

    private final String pattern;

    public PatternPropertyModel(String name, boolean optional, boolean array, ReferenceModel reference, String pattern) {
        super(name, optional, array, reference);
        this.pattern = pattern;
    }

    public PatternPropertyModel(TypeModel type, String name, boolean optional, String pattern) {
        super(type, name, optional);
        this.pattern = pattern;
    }

    public PatternPropertyModel(TypeModel type, String name, boolean optional, boolean array, String pattern) {
        super(type, name, optional, array);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
