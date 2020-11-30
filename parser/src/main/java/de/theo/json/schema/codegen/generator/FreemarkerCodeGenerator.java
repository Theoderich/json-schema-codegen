package de.theo.json.schema.codegen.generator;

import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.generator.freemarker.RootModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FreemarkerCodeGenerator {


    private final Path targetPath;
    private final String basePackage;
    private final Configuration cfg;
    private final Path basePackagePath;

    public FreemarkerCodeGenerator(Path targetPath, String basePackage, File templatesDir) throws IOException {
        this(targetPath, basePackage);
        cfg.setDirectoryForTemplateLoading(templatesDir);
    }

    public FreemarkerCodeGenerator(Path targetPath, String basePackage, String language) throws IOException {
        this(targetPath, basePackage);
        cfg.setClassForTemplateLoading(this.getClass(), "/freemarker/" + language);
    }

    private FreemarkerCodeGenerator(Path targetPath, String basePackage) throws IOException {
        this.targetPath = targetPath;
        this.basePackage = basePackage;
        String packageRelativePath = this.basePackage.replaceAll("\\.", "/");
        basePackagePath = this.targetPath.resolve(packageRelativePath);
        cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
    }

    public void generateCode(List<ClassModel> models) throws IOException, TemplateException {
        Files.createDirectories(basePackagePath);

        for (ClassModel model : models) {
            writeClass(model);
        }

    }

    private void writeClass(ClassModel model) throws IOException, TemplateException {
        RootModel rootModel = new RootModel(basePackage, model);
        String templateToUse = getTemplateToUse(rootModel);
        if (templateToUse == null || templateToUse.isEmpty()) {
            return;
        }
        String fileName = getFileName(rootModel);
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        Template template = cfg.getTemplate(templateToUse);
        try (Writer writer = Files.newBufferedWriter(basePackagePath.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            template.process(rootModel, writer);
        }
    }

    private String getTemplateToUse(RootModel rootModel) throws IOException, TemplateException {
        Template templateTemplate = cfg.getTemplate("template.ftlh");
        StringWriter out = new StringWriter();
        templateTemplate.process(rootModel, out);
        return out.toString();
    }

    private String getFileName(RootModel rootModel) throws IOException, TemplateException {
        Template fileNameTemplate = cfg.getTemplate("fileName.ftlh");
        StringWriter out = new StringWriter();
        fileNameTemplate.process(rootModel, out);
        return out.toString();
    }


}
