package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ErrorReferenceType implements ErrorBaseType {

    private final String reference;
    private ErrorType referencedObject;

    public ErrorReferenceType(String reference) {
        this.reference = reference;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ErrorType> errorRefMap) throws ParseException {
        this.referencedObject = errorRefMap.get(reference);
        if (this.referencedObject == null) {
            throw new ParseException("Could not find referenced object for " + reference);
        }
    }

    @Override
    public int getErrorCode() {
        return referencedObject.getErrorCode();
    }

    @Override
    public String getMessage() {
        return referencedObject.getMessage();
    }

    @Override
    public BaseType getData() {
        return referencedObject.getData();
    }

    @Override
    public String toString() {
        return "ErrorReferenceType{" +
                "reference='" + reference + '\'' +
                ", referencedObject=" + referencedObject +
                "} ";
    }

    public String getReference() {
        return reference;
    }

}
