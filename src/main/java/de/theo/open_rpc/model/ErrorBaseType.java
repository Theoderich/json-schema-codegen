package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public interface ErrorBaseType {

    void resolveReferences(Map<String, BaseType> refMap, Map<String, ErrorType> errorRefMap) throws ParseException;

    int getErrorCode();

    String getMessage();

    BaseType getData();

}
