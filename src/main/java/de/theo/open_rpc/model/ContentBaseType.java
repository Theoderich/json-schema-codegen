package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public interface ContentBaseType {

    void resolveReferences(Map<String, BaseType> refMap, Map<String, ContentType> contentRefMap) throws ParseException;

    String getName();

    String getDescription();

    String getSummary();

    BaseType getSchema();

    boolean isRequired();

    boolean isDeprecated();
}
