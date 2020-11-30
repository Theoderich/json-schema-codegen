package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenRpcDocument {
    private InfoType infoType;
    private List<MethodType> methods;
    private List<BaseType> schemas;
    private List<ErrorBaseType> errors;
    private List<ContentBaseType> contentTypes;

    public OpenRpcDocument(String title) {
        this.infoType = new InfoType(title);
        this.methods = new ArrayList<>();
        this.schemas = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.contentTypes = new ArrayList<>();
    }


    public void addMethod(MethodType method) {
        this.methods.add(method);
    }

    public void addSchema(BaseType schema) {
        this.schemas.add(schema);
    }

    public void addError(ErrorBaseType error) {
        this.errors.add(error);
    }

    public void addContentType(ContentBaseType contentType) {
        this.contentTypes.add(contentType);
    }

    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ContentType> contentRefMap, Map<String, ErrorType> errorRefMap) throws ParseException {
        for (BaseType schema : this.schemas) {
            schema.resolveReferences(refMap);
        }
        for (ErrorBaseType error : errors) {
            error.resolveReferences(refMap, errorRefMap);
        }
        for (ContentBaseType contentType : contentTypes) {
            contentType.resolveReferences(refMap, contentRefMap);
        }
        for (MethodType method : methods) {
            method.resolveReferences(refMap, contentRefMap, errorRefMap);
        }
    }

    public InfoType getInfoType() {
        return infoType;
    }

    public List<MethodType> getMethods() {
        return methods;
    }

    public List<BaseType> getSchemas() {
        return schemas;
    }

    public List<ErrorBaseType> getErrors() {
        return errors;
    }

    public List<ContentBaseType> getContentTypes() {
        return contentTypes;
    }

    @Override
    public String toString() {
        return "OpenRpcDocument{\n" +
                "infoType=" + infoType +
                "\nmethods=" + methods +
                "\nschemas=" + schemas +
                "\nerrors=" + errors +
                "\ncontentTypes=" + contentTypes +
                "\n}";
    }
}
