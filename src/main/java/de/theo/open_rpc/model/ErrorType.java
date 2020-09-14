package de.theo.open_rpc.model;

import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.ParseException;

import java.util.Map;

public class ErrorType extends ErrorBaseType {

    private int errorCode;
    private String message;
    private BaseType data;

    public ErrorType(int errorCode, String message, BaseType data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap, Map<String, ErrorType> errorRefMap) throws ParseException {
        if (this.data != null) {
            data.resolveReferences(refMap);
        }
    }

    @Override
    public String toString() {
        return "ErrorType{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
