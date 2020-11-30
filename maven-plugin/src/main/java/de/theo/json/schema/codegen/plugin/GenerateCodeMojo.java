package de.theo.json.schema.codegen.plugin;

import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.generator.FreemarkerCodeGenerator;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.parser.CodeModelMapper;
import de.theo.json.schema.codegen.parser.JsonSchemaParser;
import de.theo.json.schema.codegen.parser.ParseException;
import freemarker.template.TemplateException;
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
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "generate-code")
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

    @Parameter(property = "language", defaultValue = "java")
    private String language;

    public void execute() throws MojoExecutionException {
        // Use the group id as default target package
        if (StringUtils.isEmpty(targetPackage)) {
            targetPackage = project.getGroupId();
        }

        File[] inputFiles = sourceFolder.listFiles();

        if (inputFiles == null) {
            throw new MojoExecutionException("Unable to process files from input directory.");
        }


        CodeModelMapper codeModelMapper = new CodeModelMapper();
        FreemarkerCodeGenerator javaCodeGenerator = getFreemarkerCodeGenerator();
        for (File file : inputFiles) {
            try (InputStream input = new FileInputStream(file)) {
                JsonSchemaParser jsonSchemaParser = new JsonSchemaParser();
                JsonSchemaDocument document = jsonSchemaParser.parse(input);
                List<ClassModel> classModels = codeModelMapper.map(document);
                javaCodeGenerator.generateCode(classModels);
            } catch (IOException | ParseException | TemplateException e) {
                throw new MojoExecutionException("Unable to generate code, consult the errors and warnings printed above.", e);
            }
        }

    }

    private FreemarkerCodeGenerator getFreemarkerCodeGenerator() throws MojoExecutionException {
        try {
            File freemarkerTemplatePath = new File(GenerateCodeMojo.class.getResource("/freemarker/" + language).toURI());
            return new FreemarkerCodeGenerator(Paths.get(targetFolder.toURI()), targetPackage, freemarkerTemplatePath);
        } catch (URISyntaxException | IOException e) {
            throw new MojoExecutionException("unable to load templates", e);
        }
    }
}