<#-- @ftlvariable name="" type="de.theo.open_rpc.codegen.OpenRpcCodegenDefinition" -->
package ${targetPackage}

public interface ${document.infoType.title?cap_first} {

<#list document.methods as method>

    ${method.result.schema.toTypeName(targetPackage)} ${method.name?uncap_first}(
    <#list method.params as param>
        ${param.schema.toTypeName(targetPackage)} ${param.name?uncap_first}<#sep>,
    </#list>);

</#list>

}