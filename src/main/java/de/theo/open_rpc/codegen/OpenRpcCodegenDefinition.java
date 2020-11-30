package de.theo.open_rpc.codegen;

import de.theo.open_rpc.model.OpenRpcDocument;

public class OpenRpcCodegenDefinition {

    private String targetPackage;
    private OpenRpcDocument document;

    public OpenRpcCodegenDefinition(String targetPackage, OpenRpcDocument document) {
        this.targetPackage = targetPackage;
        this.document = document;
    }


    public String getTargetPackage() {
        return targetPackage;
    }

    public OpenRpcDocument getDocument() {
        return document;
    }
}
