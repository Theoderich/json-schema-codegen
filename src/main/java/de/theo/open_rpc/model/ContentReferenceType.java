package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ContentReferenceType extends ContentBaseType {

    private final String reference;
    private ContentType referencedObject;

    public ContentReferenceType(String reference) {
        this.reference = reference;
    }

    @Override
    public void resolveReferences(Map<String, ContentType> refMap) throws ParseException {
        this.referencedObject = refMap.get(reference);
        if (this.referencedObject == null) {
            throw new ParseException("Could not find referenced object for " + reference);
        }
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "ContentReferenceType{" +
                "reference='" + reference + '\'' +
                ", referencedObject=" + referencedObject +
                "} " + super.toString();
    }
}
