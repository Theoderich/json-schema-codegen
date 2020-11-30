<#-- @ftlvariable name="" type="de.theo.json.schema.codegen.generator.freemarker.RootModel" -->
<#list model.properties as property>
    <#if property.hasReference>
        <#if property.reference.isBasePackage()>
            import {${property.reference.targetName?cap_first}} from './${property.reference.targetName?cap_first}'<#t>

        </#if>
    </#if>
</#list>

export type ${model.name?cap_first} = {

<#list model.properties as property>
    ${property.name}<#if property.optional>?</#if>: <@mapType property/>
</#list>

}


<#macro mapType property>${mapTypeName(property)}<#if property.array>[]</#if></#macro>

<#function mapTypeName property>
    <#assign optional=property.optional>
    <#switch property.type>
        <#case 'INTEGER'>
        <#case 'FLOAT'>
            <#return 'number'>
        <#case 'REFERENCE'>
            <#if property.hasReference>
                <#return property.reference.targetName?cap_first>
            <#else>
                <#return 'any'>
            </#if>
        <#case 'STRING'>
            <#return 'string'>
        <#case 'BOOLEAN'>
            <#return 'boolean'>
        <#default>
            <#return property.type/>
    </#switch>
</#function>