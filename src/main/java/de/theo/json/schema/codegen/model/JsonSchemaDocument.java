package de.theo.json.schema.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class JsonSchemaDocument {

    private String schema;
    private String id;
    private String title;

    private BaseType rootClass;
    private List<BaseType> definitions;

    public JsonSchemaDocument(String schema, String id, String title) {
        this.schema = schema;
        this.id = id;
        this.title = title;
        this.definitions = new ArrayList<>();
    }

    public void setRootClass(BaseType rootClass) {
        this.rootClass = rootClass;
    }

    public void addDefinition(BaseType definition) {
        this.definitions.add(definition);
    }

    @Override
    public String toString() {
        return "JsonSchemaDocument{" +
                "schema='" + schema + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", rootClass=" + rootClass +
                ", definitions=" + definitions +
                '}';
    }
}
