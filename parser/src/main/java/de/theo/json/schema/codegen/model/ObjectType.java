package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.code.PatternPropertyModel;
import de.theo.json.schema.codegen.code.PropertyModel;
import de.theo.json.schema.codegen.code.ReferenceModel;
import de.theo.json.schema.codegen.parser.ParseException;

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
    public PropertyModel toPropertyModel(boolean optional) {
        return new PropertyModel(getName(), optional, false, new ReferenceModel("", getName()));
    }

    @Override
    public ClassModel toClassModel() {
        List<PropertyModel> properties = new ArrayList<>();
        for (BaseType member : members) {
            boolean optional = !requiredMembers.contains(member.getName());
            properties.add(member.toPropertyModel(optional));
        }
        PropertyModel additionalProperties = null;
        if (additionalMembers.isAllowed()) {
            additionalProperties = additionalMembers.getDefinition().toPropertyModel(true);
        }
        List<PatternPropertyModel> patternProperties = new ArrayList<>();
        for (PatternType patternMember : patternMembers) {
            PatternPropertyModel patternPropertyModel = new PatternPropertyModel(patternMember.getMembers().toPropertyModel(true).getType(), "", true, patternMember.getPattern());
            patternProperties.add(patternPropertyModel);
        }
        return new ClassModel(getName(), properties, additionalProperties, patternProperties);
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
