package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public abstract class ContentBaseType {
    public void resolveReferences(Map<String, ContentType> refMap) throws ParseException {

    }

    @Override
    public String toString() {
        return "ContentBaseType{}";
    }
}
