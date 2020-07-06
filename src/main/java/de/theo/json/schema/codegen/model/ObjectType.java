package de.theo.json.schema.codegen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
