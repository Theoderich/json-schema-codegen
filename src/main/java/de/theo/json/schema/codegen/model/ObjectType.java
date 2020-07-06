package de.theo.json.schema.codegen.model;

import de.theo.json.schema.codegen.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ObjectType extends BaseType {

    private List<BaseType> members;
    private List<PatternType> patternMembers;
    private AdditionalProperties additionalMembers;
    private List<String> requiredMembers;

    public ObjectType(String name, AdditionalProperties additionalMembers, List<String> requiredMembers) {
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
