package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ContentReferenceType implements ContentBaseType {

    private final String reference;
    private ContentType referencedObject;

    public ContentReferenceType(String reference) {
        this.reference = reference;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ContentType> contentRefMap) throws ParseException {
        this.referencedObject = contentRefMap.get(reference);
        if (this.referencedObject == null) {
            throw new ParseException("Could not find referenced object for " + reference);
        }
        referencedObject.resolveReferences(refMap, contentRefMap);
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String getName() {
        return referencedObject.getName();
    }

    @Override
    public String getDescription() {
        return referencedObject.getDescription();
    }

    @Override
    public String getSummary() {
        return referencedObject.getSummary();
    }

    @Override
    public BaseType getSchema() {
        return referencedObject.getSchema();
    }

    @Override
    public boolean isRequired() {
        return referencedObject.isRequired();
    }

    @Override
    public boolean isDeprecated() {
        return referencedObject.isDeprecated();
    }

    @Override
    public String toString() {
        return "ContentReferenceType{" +
                "reference='" + reference + '\'' +
                ", referencedObject=" + referencedObject +
                "} ";
    }
}
