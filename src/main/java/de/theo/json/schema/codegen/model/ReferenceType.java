package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ReferenceType extends BaseType {

    private final String reference;
    private BaseType referencedObject;

    public ReferenceType(String name, String reference) {
        super(name);
        this.reference = reference;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        if(reference.startsWith("http")){
            this.referencedObject = new AnyType("");
        } else {
            this.referencedObject = refMap.get(reference);
        }
        if(this.referencedObject == null){
            throw new ParseException("Could not find referenced object for " +  reference);
        }
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
