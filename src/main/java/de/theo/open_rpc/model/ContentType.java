package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ContentType implements ContentBaseType {

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
    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ContentType> contentRefMap) throws ParseException {
        if (schema != null) {
            schema.resolveReferences(refMap);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public BaseType getSchema() {
        return schema;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
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
