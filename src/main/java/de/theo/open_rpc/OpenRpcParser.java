package de.theo.open_rpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.parser.JsonSchemaParser;
import de.theo.json.schema.codegen.parser.ParseException;
import de.theo.open_rpc.codegen.OpenRpcCodegenDefinition;
import de.theo.open_rpc.model.ContentBaseType;
import de.theo.open_rpc.model.ContentReferenceType;
import de.theo.open_rpc.model.ContentType;
import de.theo.open_rpc.model.ErrorBaseType;
import de.theo.open_rpc.model.ErrorReferenceType;
import de.theo.open_rpc.model.ErrorType;
import de.theo.open_rpc.model.MethodType;
import de.theo.open_rpc.model.OpenRpcDocument;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class OpenRpcParser extends JsonSchemaParser {

    private static final String SOURCE_SCHEMA = "/petstore-openrpc.json";

    private final Map<String, ContentType> contentRefMap = new HashMap<>();
    private final Map<String, ErrorType> errorRefMap = new HashMap<>();


    public static void main(String[] args) throws Exception {
        try (InputStream inputStream = JsonSchemaParser.class.getResourceAsStream(SOURCE_SCHEMA)) {
            OpenRpcDocument document = new OpenRpcParser().parseOpenRpcDocument(inputStream);
            System.out.println(document);
            Configuration configuration = prepareFreemarkerConfig();
            configuration.setClassForTemplateLoading(OpenRpcParser.class, "/templates");
            Template template = configuration.getTemplate("server/spring/Api.ftl");

            OpenRpcCodegenDefinition openRpcCodegenDefinition = new OpenRpcCodegenDefinition("de.theo.generated", document);
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("C:\\projects\\json-schema-codegen\\src\\main\\java\\Api.java"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                template.process(openRpcCodegenDefinition, writer);
            }

        }
    }

    private static Configuration prepareFreemarkerConfig() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

    public OpenRpcDocument parseOpenRpcDocument(InputStream inputStream) throws ParseException {
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        if (!jsonElement.isJsonObject()) {
            throw new ParseException("root element is not a JSON Object");
        }
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonObject info = getRequiredMemberAsObject(rootObject, "info");
        JsonArray methods = getRequiredMemberAsArray(rootObject, "methods");
        JsonObject components = getOptionalMemberAsObject(rootObject, "components");

        String title = getRequiredMemberAsString(info, "title");
        OpenRpcDocument openRpcDocument = new OpenRpcDocument(title);

        for (JsonElement methodArrayItem : methods) {
            MethodType parsedMethod = parseMethod(methodArrayItem);
            openRpcDocument.addMethod(parsedMethod);
        }

        if (components != null) {
            JsonObject schemas = getOptionalMemberAsObject(components, "schemas");
            if (schemas != null) {
                for (Map.Entry<String, JsonElement> schemaEntry : schemas.entrySet()) {
                    BaseType schemaDefinition = parseDefinition(schemaEntry.getValue(), schemaEntry.getKey(), "#/components/schemas");
                    openRpcDocument.addSchema(schemaDefinition);
                }
            }
            JsonObject errors = getOptionalMemberAsObject(components, "errors");
            if (errors != null) {
                for (Map.Entry<String, JsonElement> errorEntry : errors.entrySet()) {
                    String name = errorEntry.getKey();
                    JsonObject errorObject = requireObject(name, errorEntry.getValue());
                    String refValue = "#/components/errors/" + name;
                    ErrorBaseType errorType = parseErrorType(errorObject, refValue);
                    openRpcDocument.addError(errorType);
                }
            }
            JsonObject contentDescriptors = getOptionalMemberAsObject(components, "contentDescriptors");
            if (contentDescriptors != null) {
                for (Map.Entry<String, JsonElement> contentDescriptorEntry : contentDescriptors.entrySet()) {
                    JsonObject contentDescriptorObject = requireObject(contentDescriptorEntry.getKey(), contentDescriptorEntry.getValue());
                    String refValue = "#/components/contentDescriptors/" + contentDescriptorEntry.getKey();
                    ContentBaseType contentType = parseContentType(contentDescriptorObject, refValue, refValue);
                    openRpcDocument.addContentType(contentType);
                }
            }
        }

        openRpcDocument.resolveReferences(refMap, contentRefMap, errorRefMap);
        return openRpcDocument;

    }

    private MethodType parseMethod(JsonElement methodArrayItem) throws ParseException {
        JsonObject methodObject = requireObject("methods ArrayElements", methodArrayItem);
        String methodName = getRequiredMemberAsString(methodObject, "name");
        String summary = getOptionalMemberAsString(methodObject, "summary");
        String description = getOptionalMemberAsString(methodObject, "description");
        boolean deprecated = getOptionalMemberAsBool(methodObject, "deprecated", false);
        JsonArray params = getOptionalMemberAsArray(methodObject, "params");
        JsonObject result = getRequiredMemberAsObject(methodObject, "result");
        JsonArray errors = getOptionalMemberAsArray(methodObject, "errors");

        ContentBaseType resultContent = parseContentTypeInline(result, "#/methods/" + methodName + "/result");
        MethodType methodType = new MethodType(methodName, description, summary, resultContent, deprecated);
        if (errors != null) {
            int errorIndex = 0;
            for (JsonElement error : errors) {
                JsonObject errorObject = requireObject("errors ArrayElements", error);
                ErrorBaseType parsedError = parseErrorType(errorObject, "#/methods/" + methodName + "/errors/" + errorIndex);
                methodType.addError(parsedError);
                ++errorIndex;
            }
        }
        if (params != null) {
            for (JsonElement param : params) {
                JsonObject paramObject = requireObject("param ArrayElements", param);
                ContentBaseType parsedParam = parseContentTypeInline(paramObject, "#/methods/" + methodName + "/params");
                methodType.addParam(parsedParam);
            }
        }
        return methodType;
    }

    private ErrorBaseType parseErrorType(JsonObject errorObject, String errorRef) throws ParseException {
        String $ref = getOptionalMemberAsString(errorObject, "$ref");
        if ($ref != null) {
            return new ErrorReferenceType($ref);
        }
        int errorCode = getRequiredMemberAsInteger(errorObject, "code");
        String message = getRequiredMemberAsString(errorObject, "message");
        JsonObject data = getOptionalMemberAsObject(errorObject, "data");
        BaseType dataType = null;
        if (data != null) {
            dataType = parseDefinition(data, "", errorRef + "/data");
        }
        ErrorType errorType = new ErrorType(errorCode, message, dataType);
        errorRefMap.put(errorRef, errorType);
        return errorType;
    }

    private ContentBaseType parseContentTypeInline(JsonObject jsonObject, String parentRef) throws ParseException {
        String name = getOptionalMemberAsString(jsonObject, "name");
        return parseContentType(jsonObject, parentRef + "/" + name + "/schema", parentRef + "/" + name);
    }

    private ContentBaseType parseContentType(JsonObject jsonObject, String schemaRef, String contentRef) throws ParseException {
        String $ref = getOptionalMemberAsString(jsonObject, "$ref");
        if ($ref != null) {
            return new ContentReferenceType($ref);
        }
        String name = getRequiredMemberAsString(jsonObject, "name");
        String summary = getOptionalMemberAsString(jsonObject, "summary");
        String description = getOptionalMemberAsString(jsonObject, "description");
        boolean required = getOptionalMemberAsBool(jsonObject, "required", false);
        boolean deprecated = getOptionalMemberAsBool(jsonObject, "deprecated", false);
        JsonObject schema = getRequiredMemberAsObject(jsonObject, "schema");
        BaseType schemaDefinition = parseDefinition(schema, name, schemaRef);
        ContentType contentType = new ContentType(name, description, summary, schemaDefinition, required, deprecated);
        contentRefMap.put(contentRef, contentType);
        return contentType;
    }


}