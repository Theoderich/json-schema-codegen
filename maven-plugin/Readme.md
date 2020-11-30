# Json Schema Codgen Maven Plugin

A maven plugin for generating code from a json schema.

## Goals
The plugin has only one goal: "generate-code".

## Usage

Simply add the plugin to the pom.xml of your maven module and if necessary, configure the 
- source file (default `src/main/resources/json-schema.json`)
- target folder (default `target/generated/json-codegen`)
- target package (defaults to the group id of the project)
as below.


    <plugin>
        <groupId>de.qaware</groupId>
        <artifactId>json-schema-codegen-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <targetPackage>de.example</targetPackage>
            <sourceFile>src/main/resources/json-schema.json</sourceFile>
            <targetFolder>target/generated/json-codegen</targetFolder>
        </configuration>
    </plugin>

    
