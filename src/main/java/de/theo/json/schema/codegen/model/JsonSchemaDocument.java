package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        this.rootClass.resolveReferences(refMap);
        for (BaseType definition : this.definitions) {
            definition.resolveReferences(refMap);
        }
    }

    @Override
    public String toString() {
        return "JsonSchemaDocument{" +
                "schema='" + schema + '\'' +
                "\nid='" + id + '\'' +
                "\ntitle='" + title + '\'' +
                "\nrootClass=" + rootClass +
                "\ndefinitions=" + definitions +
                '}';
    }
}
