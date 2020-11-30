package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodType {

    private String name;
    private String description;
    private String summary;
    private List<ContentBaseType> params;
    private ContentBaseType result;
    private List<ErrorBaseType> errors;
    private boolean deprecated;

    public MethodType(String name, String description, String summary, ContentBaseType result, boolean deprecated) {
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.result = result;
        this.deprecated = deprecated;
        this.params = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ContentType> contentRefMap, Map<String, ErrorType> errorRefMap) throws ParseException {
        result.resolveReferences(refMap, contentRefMap);
        for (ContentBaseType param : params) {
            param.resolveReferences(refMap, contentRefMap);
        }
        for (ErrorBaseType error : errors) {
            error.resolveReferences(refMap, errorRefMap);
        }
    }

    public void addParam(ContentBaseType param) {
        this.params.add(param);
    }

    public void addError(ErrorBaseType error) {
        this.errors.add(error);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
    }

    public List<ContentBaseType> getParams() {
        return params;
    }

    public ContentBaseType getResult() {
        return result;
    }

    public List<ErrorBaseType> getErrors() {
        return errors;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public String toString() {
        return "MethodType{" +
                "\nname='" + name + '\'' +
                "\ndescription='" + description + '\'' +
                "\nsummary='" + summary + '\'' +
                "\nparams=" + params +
                "\nresult=" + result +
                "\nerrors=" + errors +
                "\ndeprecated=" + deprecated +
                "\n}";
    }


}
