package de.theo.json.schema.codegen.generator;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class GeneratorUtil {

    private GeneratorUtil() {
    }

    public static String toFirstCharUpper(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase(Locale.US);
        }
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1);
    }

    public static String asAnnotationValue(String str) {
        return '"' + str.replaceAll("\\$", "\\$\\$") + '"';
    }

    public static String toEnumValue(String str) {
        String result = str.replaceAll("\\W", "_").toUpperCase(Locale.US);
        if (!Character.isLetter(result.charAt(0))) {
            return "_" + result;
        }
        return result;
    }

    public static void addJsonProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName, String jsonName) {
        FieldSpec.Builder fieldBuilder = addProperty(typeBuilder, fieldType, fieldName);
        fieldBuilder.addAnnotation(AnnotationSpec.builder(JsonProperty.class).addMember("value", GeneratorUtil.asAnnotationValue(jsonName)).build());
        typeBuilder.addField(fieldBuilder.build());
    }

    public static void addJsonAnyProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName) {
        ParameterizedTypeName mapFieldType = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), fieldType);
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(mapFieldType, fieldName, Modifier.PRIVATE);
        MethodSpec getter = createGetter(fieldName, mapFieldType)
                .addAnnotation(AnnotationSpec.builder(JsonAnyGetter.class).build())
                .build();
        MethodSpec setter = createSetter(fieldName, mapFieldType).build();
        MethodSpec adder = createMapAdder(fieldName, ClassName.get(String.class), fieldType)
                .addAnnotation(AnnotationSpec.builder(JsonAnySetter.class).build())
                .build();
        typeBuilder.addField(fieldBuilder.build());
        typeBuilder.addMethod(getter).addMethod(setter).addMethod(adder);
    }

    private static FieldSpec.Builder addProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName) {
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE);
        GeneratorUtil.addGetterAndSetter(typeBuilder, fieldName, fieldType);
        return fieldBuilder;
    }

    public static void addGetterAndSetter(TypeSpec.Builder typeBuilder, String propertyName, TypeName propertyType) {
        MethodSpec getter = createGetter(propertyName, propertyType).build();
        MethodSpec setter = createSetter(propertyName, propertyType).build();
        typeBuilder.addMethod(getter).addMethod(setter);
    }

    private static MethodSpec.Builder createSetter(String fieldName, TypeName fieldType) {
        String setterMethodName = methodNameForField("set", fieldName);
        return MethodSpec.methodBuilder(setterMethodName)
                .addParameter(fieldType, fieldName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$1L = $1L", fieldName);
    }

    private static MethodSpec.Builder createGetter(String fieldName, TypeName fieldType) {
        String getterMethodName = methodNameForField("get", fieldName);
        return MethodSpec.methodBuilder(getterMethodName)
                .returns(fieldType)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$L", fieldName);
    }

    private static MethodSpec.Builder createMapAdder(String fieldName, TypeName keyType, TypeName valueType) {
        String adderMethodName = methodNameForField("add", fieldName);
        return MethodSpec.methodBuilder(adderMethodName)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(keyType, "key")
                .addParameter(valueType, "value")
                .addCode(
                        CodeBlock.builder()
                                .beginControlFlow("if (this.$L == null)", fieldName)
                                .add("this.$L = new $T<>();", fieldName, ClassName.get(HashMap.class))
                                .endControlFlow()
                                .add("this.$L.put(key, value);", fieldName)
                                .build()
                );

    }

    private static String methodNameForField(String prefix, String propertyName) {
        return prefix + propertyName.substring(0, 1).toUpperCase(Locale.US) + propertyName.substring(1);
    }

    public static void addToString(TypeSpec.Builder typeBuilder, String typeName) {
        MethodSpec.Builder toStringMethod = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class);
        StringBuilder sb = new StringBuilder();
        sb.append("return \"").append(typeName).append(": {\\n\"");
        for (FieldSpec fieldSpec : typeBuilder.fieldSpecs) {
            String fieldName = fieldSpec.name.replace("$", "$$");
            sb.append(" +\n\"").append(fieldName).append(": \" + this.").append(fieldName).append(" + \"\\n\"");
        }
        sb.append(" +\n\"}\"");
        toStringMethod.addStatement(sb.toString());
        typeBuilder.addMethod(toStringMethod.build());
    }

}
