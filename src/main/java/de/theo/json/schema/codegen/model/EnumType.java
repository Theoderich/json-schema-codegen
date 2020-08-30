package de.theo.json.schema.codegen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import de.theo.json.schema.codegen.generator.GeneratorUtil;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

public class EnumType extends BaseType implements ModelType {

    private final List<String> enumValues;

    public EnumType(String title, List<String> enumValues) {
        super(title);
        this.enumValues = enumValues;
    }

    public List<String> getEnumValues() {
        return Collections.unmodifiableList(enumValues);
    }

    @Override
    public ClassName toTypeName(String targetPackage) {
        return ClassName.get(targetPackage, GeneratorUtil.toFirstCharUpper(this.getName()));
    }

    @Override
    public String toString() {
        return "EnumType{" +
                "enumValues=" + enumValues +
                "} " + super.toString();
    }

    @Override
    public JavaFile toJavaFile(String targetPackage) {
        ClassName className = this.toTypeName(targetPackage);
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(className);
        enumBuilder.addModifiers(Modifier.PUBLIC);
        for (String value : enumValues) {
            AnnotationSpec annotation = AnnotationSpec.builder(JsonProperty.class).addMember("value", GeneratorUtil.asAnnotationValue(value)).build();
            TypeSpec annotated = TypeSpec.anonymousClassBuilder("").addAnnotation(annotation).build();
            enumBuilder.addEnumConstant(GeneratorUtil.toEnumValue(value), annotated);
        }
        return JavaFile.builder(targetPackage, enumBuilder.build()).build();
    }
}
