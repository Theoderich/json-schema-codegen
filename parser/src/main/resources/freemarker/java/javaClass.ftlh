<#-- @ftlvariable name="" type="de.theo.json.schema.codegen.generator.freemarker.RootModel" -->
package ${targetPackage};

<#list model.properties as property>
    <#if property.hasReference>
        <#if property.reference.isBasePackage()>
        <#else>
            import ${property.reference.targetPackage}.${property.reference.targetName?cap_first};<#t>

        </#if>
    </#if>
</#list>
import com.fasterxml.jackson.annotation.JsonProperty;
<#if model.hasAdditionalProperties || model.hasPatternProperties>
    import com.fasterxml.jackson.annotation.JsonAnySetter;<#t>

    import java.util.Map;<#t>

</#if>


public class ${model.name?cap_first} {

<#list model.properties as property>
    @JsonProperty(value="${property.name}")
    private <@mapType property/> ${property.name?uncap_first};
</#list>
<#if model.hasAdditionalProperties || model.hasPatternProperties>

    @JsonAnySetter
    private Map<String, <@mapType model.additionalProperties/>> additionalProperties;
</#if>

<#list model.properties as property>
    public <@mapType property/> get${property.name?cap_first}(){
        return this.${property.name?uncap_first};
    }

    public void set${property.name?cap_first}(<@mapType property/> ${property.name?uncap_first}){
        this.${property.name?uncap_first} = ${property.name?uncap_first};
    }
</#list>

<#if model.hasAdditionalProperties || model.hasPatternProperties>
    public Map<String, <@mapType model.additionalProperties/>> getAdditionalProperties(){
        return this.additionalProperties;
    }

    public void setAdditionalProperties(Map<String, <@mapType model.additionalProperties/>> additionalProperties){
        this.additionalProperties = additionalProperties;
    }
</#if>

}


<#macro mapType property>${mapTypeName(property)}<#if property.array>[]</#if></#macro>

<#function mapTypeName property>
    <#assign optional=property.optional>
    <#switch property.type>
        <#case 'INTEGER'>
            <#if optional>
                <#return 'Integer'>
            <#else >
                <#return 'int'>
            </#if>
        <#case 'FLOAT'>
            <#if optional>
                <#return 'Double'>
            <#else >
                <#return 'double'>
            </#if>
        <#case 'REFERENCE'>
            <#if property.hasReference>
                <#return property.reference.targetName?cap_first>
            <#else>
                <#return 'Object'>
            </#if>
        <#case 'STRING'>
            <#return 'String'>
        <#case 'BOOLEAN'>
            <#if optional>
                <#return 'Boolean'>
            <#else >
                <#return 'boolean'>
            </#if>
        <#default>
            <#return property.type/>
    </#switch>
</#function>