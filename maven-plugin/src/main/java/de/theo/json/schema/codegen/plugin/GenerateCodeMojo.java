package de.theo.json.schema.codegen.plugin;

import de.theo.json.schema.codegen.generator.JavaCodeGenerator;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.parser.JsonSchemaParser;
import de.theo.json.schema.codegen.parser.ParseException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Mojo(name="generate-code")
public class GenerateCodeMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    // The folder containing the json schemas given by an absolute path or a path relative to the pom
    @Parameter(defaultValue = "src/main/resources/json-schema", property = "sourceFolder")
    private File sourceFolder;

    // The target folder for the generated code given by an absolute path or a path relative to the pom
    @Parameter(defaultValue = "target/generated/json-codegen", property = "targetFolder")
    private File targetFolder;

    // The package of the generated code
    @Parameter(property = "targetPackage")
    private String targetPackage;

    public void execute() throws MojoExecutionException {
        // Use the group id as default target package
        if (StringUtils.isEmpty(targetPackage)) {
            targetPackage = project.getGroupId();
        }

        File[] inputFiles = sourceFolder.listFiles();

        if (inputFiles == null) {
            throw new MojoExecutionException("Unable to process files from input directory.");
        }

        for (File file : inputFiles) {
            try (InputStream input = new FileInputStream(file)) {
                JsonSchemaParser jsonSchemaParser = new JsonSchemaParser();
                JsonSchemaDocument document = jsonSchemaParser.parse(input);
                JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator(Paths.get(targetFolder.toURI()), targetPackage);
                javaCodeGenerator.generateCode(document);
            } catch (IOException | ParseException e) {
                throw new MojoExecutionException("Unable to generate code, consult the errors and warnings printed above.", e);
            }
        }

    }

}
