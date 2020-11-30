package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.JavaFile;

public interface ModelType {

    JavaFile toJavaFile(String targetPackage);

}
