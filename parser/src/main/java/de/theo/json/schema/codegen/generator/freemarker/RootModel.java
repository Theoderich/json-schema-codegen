package de.theo.json.schema.codegen.generator.freemarker;

import de.theo.json.schema.codegen.code.ClassModel;

public class RootModel {

    private String targetPackage;

    private ClassModel model;

    public RootModel(String targetPackage, ClassModel model) {
        this.targetPackage = targetPackage;
        this.model = model;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public ClassModel getModel() {
        return model;
    }

}
