package de.theo.json.schema.codegen.parser;

import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.model.ModelType;

import java.util.ArrayList;
import java.util.List;

public class CodeModelMapper {

    public List<ClassModel> map(JsonSchemaDocument jsonSchemaDocument) {
        List<ClassModel> result = new ArrayList<>();
        for (BaseType type : jsonSchemaDocument.getAllTypes()) {
            mapType(result, type);
        }

        return result;
    }

    private void mapType(List<ClassModel> result, BaseType type) {
        if (type instanceof ModelType) {
            result.add(((ModelType) type).toClassModel());
        }
        for (BaseType child : type.children()) {
            mapType(result, child);
        }
    }
}
