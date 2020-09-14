package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;

public class ContentType extends ContentBaseType {

    private String name;
    private String description;
    private String summary;
    private BaseType schema;
    private boolean required;
    private boolean deprecated;

    public ContentType(String name, String description, String summary, BaseType schema, boolean required, boolean deprecated) {
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.schema = schema;
        this.required = required;
        this.deprecated = deprecated;
    }

    @Override
    public String toString() {
        return "ContentType{" +
                "\nname='" + name + '\'' +
                "\ndescription='" + description + '\'' +
                "\nsummary='" + summary + '\'' +
                "\nschema=" + schema +
                "\nrequired=" + required +
                "\ndeprecated=" + deprecated +
                "\n}";
    }
}
