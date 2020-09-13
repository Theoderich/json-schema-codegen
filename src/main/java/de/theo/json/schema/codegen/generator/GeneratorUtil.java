package de.theo.json.schema.codegen.generator;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Locale;
import java.util.Map;

public final class GeneratorUtil {

    private GeneratorUtil() {
    }

    public static String toFirstCharUpper(String str){
        if(str == null || str.isEmpty()){
            return str;
        }
        if(str.length() == 1){
            return str.toUpperCase(Locale.US);
        }
        return str.substring(0,1).toUpperCase(Locale.US) + str.substring(1);
    }

    public static String asAnnotationValue(String str){
        return '"' + str.replaceAll("\\$", "\\$\\$") + '"';
    }

    public static String toEnumValue(String str){
        String result = str.replaceAll("\\W", "_").toUpperCase(Locale.US);
        if(!Character.isLetter(result.charAt(0))){
            return "_" + result;
        }
        return result;
    }

    public static void addJsonProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName, String jsonName){
        FieldSpec.Builder fieldBuilder = addProperty(typeBuilder, fieldType, fieldName);
        fieldBuilder.addAnnotation(AnnotationSpec.builder(JsonProperty.class).addMember("value", GeneratorUtil.asAnnotationValue(jsonName)).build());
        typeBuilder.addField(fieldBuilder.build());
    }

    public static void addJsonAnyProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName){
        FieldSpec.Builder fieldBuilder = addProperty(typeBuilder, ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class), fieldType), fieldName);
        fieldBuilder.addAnnotation(AnnotationSpec.builder(JsonAnySetter.class).build());
        typeBuilder.addField(fieldBuilder.build());
    }

    private static FieldSpec.Builder addProperty(TypeSpec.Builder typeBuilder, TypeName fieldType, String fieldName) {
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE);
        GeneratorUtil.addGetterAndSetter(typeBuilder, fieldName, fieldType);
        return fieldBuilder;
    }

    public static void addGetterAndSetter(TypeSpec.Builder typeBuilder, String propertyName, TypeName propertyType) {
        String firstUpperPropertyName = propertyName.substring(0, 1).toUpperCase(Locale.US) + propertyName.substring(1);
        MethodSpec getter = MethodSpec.methodBuilder("get" + firstUpperPropertyName)
                .returns(propertyType)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$L", propertyName).build();
        MethodSpec setter = MethodSpec.methodBuilder("set" + firstUpperPropertyName)
                .addParameter(propertyType, propertyName)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$1L = $1L", propertyName).build();
        typeBuilder.addMethod(getter).addMethod(setter);
    }

}
