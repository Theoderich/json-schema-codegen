package de.theo.json.schema.codegen.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.NameAllocator;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import de.theo.json.schema.codegen.generator.GeneratorUtil;
import de.theo.json.schema.codegen.parser.ParseException;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectType extends BaseType implements ModelType {

    private List<BaseType> members;
    private List<PatternType> patternMembers;
    private AdditionalProperties additionalMembers;
    private Set<String> requiredMembers;

    public ObjectType(String name, AdditionalProperties additionalMembers, Set<String> requiredMembers) {
        super(name);
        this.additionalMembers = additionalMembers;
        this.requiredMembers = requiredMembers;
        this.members = new ArrayList<>();
        this.patternMembers = new ArrayList<>();
    }

    @Override
    public void resolveReferences(Map<String, BaseType> refMap) throws ParseException {
        for (BaseType baseType : this.members) {
            baseType.resolveReferences(refMap);
        }
        for (PatternType member : this.patternMembers) {
            member.resolveReferences(refMap);
        }
        additionalMembers.resolveReferences(refMap);
    }

    @Override
    public ClassName toTypeName(String targetPackage) {
        return ClassName.get(targetPackage, GeneratorUtil.toFirstCharUpper(this.getName()));
    }


    @Override
    public JavaFile toJavaFile(String targetPackage) {
        ClassName className = this.toTypeName(targetPackage);
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className);
        typeBuilder.addModifiers(Modifier.PUBLIC);
        NameAllocator nameAllocator = new NameAllocator();
        for (BaseType member : members) {
            String jsonName = member.getName();
            String fieldName = nameAllocator.newName(jsonName);
            TypeName fieldType = member.toTypeName(targetPackage);
            if(!requiredMembers.contains(jsonName)){
                fieldType = fieldType.box();
            }
            GeneratorUtil.addJsonProperty(typeBuilder, fieldType, fieldName, jsonName);
        }

        if(additionalMembers.isAllowed() || patternMembers.size() > 0){
            String additionalPropertiesName = nameAllocator.newName("additionalProperties");
            if(additionalMembers.isAllowed() && patternMembers.isEmpty()){
                TypeName fieldType = additionalMembers.getDefinition().toTypeName(targetPackage);
                GeneratorUtil.addJsonAnyProperty(typeBuilder, fieldType, additionalPropertiesName);
            } else if (!additionalMembers.isAllowed() && patternMembers.size() == 1) {
                TypeName fieldType = patternMembers.get(0).getMembers().toTypeName(targetPackage);
                GeneratorUtil.addJsonAnyProperty(typeBuilder, fieldType, additionalPropertiesName);
            } else {
                GeneratorUtil.addJsonAnyProperty(typeBuilder, TypeName.OBJECT, additionalPropertiesName);
            }
        } 

        TypeSpec typeSpec = typeBuilder.build();
        return JavaFile.builder(targetPackage, typeSpec).build();
    }

    @Override
    public List<BaseType> children() {
        return new ArrayList<>(members);
    }

    public List<BaseType> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public void addMember(BaseType member) {
        members.add(member);
    }

    public List<PatternType> getPatternMembers() {
        return Collections.unmodifiableList(patternMembers);
    }

    public void addPatternMember(PatternType member) {
        patternMembers.add(member);
    }


    @Override
    public String toString() {
        return "ObjectType{" +
                "members=" + members +
                ", patternMembers=" + patternMembers +
                ", additionalMembers=" + additionalMembers +
                ", requiredMembers=" + requiredMembers +
                "} " + super.toString();
    }
}
