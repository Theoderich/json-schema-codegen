package de.qaware.json.schema.codegen.plugin.gradle

import de.theo.json.schema.codegen.code.ClassModel
import de.theo.json.schema.codegen.generator.FreemarkerCodeGenerator
import de.theo.json.schema.codegen.model.JsonSchemaDocument
import de.theo.json.schema.codegen.parser.CodeModelMapper
import de.theo.json.schema.codegen.parser.JsonSchemaParser
import de.theo.json.schema.codegen.parser.ParseException
import freemarker.template.TemplateException
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

class GenerateCodeTask extends DefaultTask {

    private String sourceFolderUrl = "src/main/resources/json-schema";

    private String targetFolderUrl = "target/generated/json-codegen";

    private String targetPackage = "de.default";

    private String language = "java";

    private String customTemplateDir = ""

    @TaskAction
    def generateCode() {
//        // Use the group id as default target package
//        if (StringUtils.isEmpty(targetPackage)) {
//            targetPackage = project.getGroupId();
//        }

        File[] inputFiles = new File(sourceFolderUrl).listFiles()

        if (inputFiles == null) {
            throw new GradleException("Unable to process files from input directory.");
        }

        CodeModelMapper codeModelMapper = new CodeModelMapper();
        FreemarkerCodeGenerator javaCodeGenerator = getFreemarkerCodeGenerator();

        for (File file : inputFiles) {
            InputStream input
            try {
                input = new FileInputStream(file)
                JsonSchemaParser jsonSchemaParser = new JsonSchemaParser();
                JsonSchemaDocument document = jsonSchemaParser.parse(input);
                List<ClassModel> classModels = codeModelMapper.map(document);
                javaCodeGenerator.generateCode(classModels);
            } catch (IOException | ParseException | TemplateException e) {
                throw new GradleException("Unable to generate code, consult the errors and warnings printed above.", e);
            } finally {
                input.close()
            }
        }
    }

    private FreemarkerCodeGenerator getFreemarkerCodeGenerator() throws GradleException {
        try {
            if (language.equals("custom")) {
                return new FreemarkerCodeGenerator(Paths.get(targetFolderUrl), targetPackage, new File(customTemplateDir))
            } else {
                return new FreemarkerCodeGenerator(Paths.get(targetFolderUrl), targetPackage, language)
            }
        } catch (IOException e) {
            throw new GradleException("unable to load templates", e);
        }
    }
}