package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public abstract class ErrorBaseType {
    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ErrorType> errorRefMap) throws ParseException {

    }

    @Override
    public String toString() {
        return "ErrorBaseType{}";
    }
}
