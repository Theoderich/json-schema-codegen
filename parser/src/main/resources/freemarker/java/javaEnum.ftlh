<#-- @ftlvariable name="" type="de.theo.json.schema.codegen.generator.freemarker.RootModel" -->
package ${targetPackage};

import com.fasterxml.jackson.annotation.JsonProperty;


public enum ${model.name?cap_first} {
<#list model.enumValues as enumval>
    @JsonProperty(value="${enumval}")
    ${escapeEnumVal(enumval)}<#sep>,
</#list>

}
<#function escapeEnumVal enumval>
    <#assign result=enumval>
    <#if !enumval?matches('$[a-zA-Z_].*')>
        <#assign result='_' + enumval>
    </#if>
    <#return result?replace('\\W', '_', 'r')>
</#function>