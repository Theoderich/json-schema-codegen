package de.theo.json.schema.codegen.code;

public class ReferenceModel {

    private String targetPackage;
    private String targetName;

    public ReferenceModel(String targetPackage, String targetName) {
        this.targetPackage = targetPackage;
        this.targetName = targetName;
    }

    public boolean isBasePackage() {
        return targetPackage.isEmpty();
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public String getTargetName() {
        return targetName;
    }
}
