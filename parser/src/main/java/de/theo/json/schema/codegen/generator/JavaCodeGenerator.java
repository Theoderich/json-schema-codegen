package de.theo.json.schema.codegen.generator;

import com.squareup.javapoet.JavaFile;
import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.model.ModelType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaCodeGenerator {


    private final Path targetPath;
    private final String basePackage;

    public JavaCodeGenerator(Path targetPath, String basePackage) {
        this.targetPath = targetPath;
        this.basePackage = basePackage;
    }

    public void generateCode(JsonSchemaDocument document) throws IOException {
        String packageRelativePath = this.basePackage.replaceAll("\\.", "/");
        Path basePackagePath = this.targetPath.resolve(packageRelativePath);
        Files.createDirectories(basePackagePath);
        for (BaseType type : document.getAllTypes()) {
            writeType(type);
        }

    }

    private void writeType(BaseType type) throws IOException {
        if (type instanceof ModelType) {
            JavaFile javaFile = ((ModelType) type).toJavaFile(this.basePackage);
            javaFile.writeToPath(this.targetPath, StandardCharsets.UTF_8);
        }
        for (BaseType child : type.children()) {
            writeType(child);
        }
    }

}
