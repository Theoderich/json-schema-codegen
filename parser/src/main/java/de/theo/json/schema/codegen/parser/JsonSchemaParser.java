package de.theo.json.schema.codegen.parser;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import de.theo.json.schema.codegen.code.ClassModel;
import de.theo.json.schema.codegen.generator.FreemarkerCodeGenerator;
import de.theo.json.schema.codegen.model.AdditionalProperties;
import de.theo.json.schema.codegen.model.AllOf;
import de.theo.json.schema.codegen.model.AnyType;
import de.theo.json.schema.codegen.model.ArrayType;
import de.theo.json.schema.codegen.model.BaseType;
import de.theo.json.schema.codegen.model.BooleanType;
import de.theo.json.schema.codegen.model.DoubleType;
import de.theo.json.schema.codegen.model.EnumType;
import de.theo.json.schema.codegen.model.IntegerType;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.model.NotType;
import de.theo.json.schema.codegen.model.NullType;
import de.theo.json.schema.codegen.model.ObjectType;
import de.theo.json.schema.codegen.model.OneOf;
import de.theo.json.schema.codegen.model.PatternType;
import de.theo.json.schema.codegen.model.ReferenceType;
import de.theo.json.schema.codegen.model.StringType;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonSchemaParser {

    private static final String SOURCE_SCHEMA = "/open-rpc-meta-schema.json";
    private Map<String, BaseType> refMap = new HashMap<>();

    public static void main(String[] args) throws IOException, ParseException, TemplateException, URISyntaxException {
        try (InputStream inputStream = JsonSchemaParser.class.getResourceAsStream(SOURCE_SCHEMA)) {
            JsonSchemaParser jsonSchemaParser = new JsonSchemaParser();
            JsonSchemaDocument parse = jsonSchemaParser.parse(inputStream);
            List<ClassModel> models = new CodeModelMapper().map(parse);

            Path basePath = Paths.get(JsonSchemaParser.class.getResource("/").toURI());
            FreemarkerCodeGenerator javaCodeGenerator = new FreemarkerCodeGenerator(
                    basePath.resolve("../generated-sources"),
                    "de.qaware.generated",
                    new File(JsonSchemaParser.class.getResource("/freemarker/java").toURI()));
            javaCodeGenerator.generateCode(models);
        }
    }


    public JsonSchemaDocument parse(InputStream in) throws ParseException {
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(in));
        if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean()){
            JsonSchemaDocument jsonSchemaDocument = new JsonSchemaDocument(null, null, null);
            if(jsonElement.getAsBoolean()){
                jsonSchemaDocument.setRootClass(new AnyType("root"));
            } else {
                jsonSchemaDocument.setRootClass(null);
            }
            return jsonSchemaDocument;
        }
        if (!jsonElement.isJsonObject()) {
            throw new ParseException("root element is not a JSON Object");
        }
        JsonObject rootObject = jsonElement.getAsJsonObject();

        String schema = getOptionalMemberAsString(rootObject, "$schema");
        String id = getOptionalMemberAsString(rootObject, "$id");
        String title = getOptionalMemberAsString(rootObject, "title");
        JsonSchemaDocument jsonSchemaDocument = new JsonSchemaDocument(schema, id, title);

        JsonObject definitions = getOptionalMemberAsObject(rootObject, "definitions");

        String rootObjectName = getOptionalMemberAsString(rootObject, "title");
        if (rootObjectName == null) {
            rootObjectName = "root";
        }
        BaseType rootClass = parseDefinition(rootObject, rootObjectName, "#");
        jsonSchemaDocument.setRootClass(rootClass);

        if (definitions != null) {
            Set<Map.Entry<String, JsonElement>> entries = requireObject("definitions", definitions).entrySet();
            for (Map.Entry<String, JsonElement> entry : entries) {
                String name = entry.getKey();
                BaseType baseType = parseDefinition(entry.getValue(), name, "#/definitions");
                jsonSchemaDocument.addDefinition(baseType);
            }
        }

        jsonSchemaDocument.resolveReferences(refMap);

        return jsonSchemaDocument;
    }

    private BaseType parseDefinition(JsonElement element, String title, String parentRef) throws ParseException {
        String curRef = parentRef + "/" + title;
        BaseType result = parseDefinitionInternal(element, title, curRef);
        if (result != null && !title.isEmpty()) {
            refMap.put(curRef, result);
        }
        return result;
    }

    private BaseType parseDefinitionInternal(JsonElement element, String title, String curRef) throws ParseException {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("$defs")) {
                JsonElement definitions = object.get("$defs");
                for (Map.Entry<String, JsonElement> e : definitions.getAsJsonObject().entrySet()) {
                    parseDefinition(e.getValue(), e.getKey(), curRef + "/$defs");
                }
            }
            if (object.has("definitions")) {
                JsonElement definitions = object.get("definitions");
                for (Map.Entry<String, JsonElement> e : definitions.getAsJsonObject().entrySet()) {
                    parseDefinition(e.getValue(), e.getKey(), curRef + "/definitions");
                }
            }
        }

        if (title.equals("$ref") && element.isJsonPrimitive()) {
            String ref = requireString(title, element);
            return new ReferenceType(title, ref);
        }
        if ((title.equals("oneOf") || title.equals("allOf")) && element.isJsonArray()) {
            Iterator<JsonElement> iterator = element.getAsJsonArray().iterator();
            ArrayList<BaseType> types = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonElement next = iterator.next();
                types.add(parseDefinition(next, "", curRef));
            }
            if (title.equals("oneOf")) {
                return new OneOf(title, types);
            } else {
                return new AllOf(title, types);
            }
        }
        if (title.equals("not") && element.isJsonObject()) {
            JsonObject notElement = element.getAsJsonObject();
            BaseType notType = parseDefinition(notElement, "", curRef);
            return new NotType(title, notType);
        }
        JsonObject object = requireObject(title, element);
        if (object.has("type") && object.get("type").isJsonArray()) {
            //Not really handling this
            return new AnyType(title);
        }
        String type = getOptionalMemberAsString(object, "type");
        if (type == null && object.has("$ref")) {
            String ref = getRequiredMemberAsString(object, "$ref");
            return new ReferenceType(title, ref);
        }
        if (type == null) {
            return new AnyType(title);
        }
        if (type.equals("null")) {
            return new NullType();
        }

        List<String> enumValues = getOptionalMemberAsStringArray(object, "enum");
        if (!enumValues.isEmpty()) {
            return new EnumType(title, enumValues);
        }
        if (type.equals("string")) {
            Integer minLength = getOptionalMemberAsInteger(object, "minLength");
            Integer maxLength = getOptionalMemberAsInteger(object, "maxLength");
            String pattern = getOptionalMemberAsString(object, "pattern");
            return new StringType(title, minLength, maxLength, pattern);
        }
        if (type.equals("boolean")) {
            return new BooleanType(title);
        }
        if (type.equals("integer") || type.equals("number")) {
            Integer multipleOf = getOptionalMemberAsInteger(object, "multipleOf");
            Integer maximum = getOptionalMemberAsInteger(object, "maximum");
            Integer exclusiveMaximum = getOptionalMemberAsInteger(object, "exclusiveMaximum");
            Integer minimum = getOptionalMemberAsInteger(object, "minimum");
            Integer exclusiveMinimum = getOptionalMemberAsInteger(object, "exclusiveMinimum");
            if (type.equals("integer")) {
                return new IntegerType(title, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
            } else {
                return new DoubleType(title, multipleOf, minimum, exclusiveMinimum, maximum, exclusiveMaximum);
            }
        }
        if (type.equals("object")) {
            AdditionalProperties additionalProperties = parseAdditionalProperties(object, curRef);
            List<String> requiredProperties = getOptionalMemberAsStringArray(object, "required");
            ObjectType objectType = new ObjectType(title, additionalProperties, new HashSet<>(requiredProperties));
            JsonObject properties = getOptionalMemberAsObject(object, "properties");
            if (properties != null) {
                for (Map.Entry<String, JsonElement> keyValuePair : properties.entrySet()) {
                    String propertyName = keyValuePair.getKey();
                    BaseType property = parseDefinition(keyValuePair.getValue(), propertyName, curRef);
                    objectType.addMember(property);
                }
            }
            JsonObject patternProperties = getOptionalMemberAsObject(object, "patternProperties");
            if (patternProperties != null) {

                for (Map.Entry<String, JsonElement> keyValuePair : patternProperties.entrySet()) {
                    PatternType patternType = new PatternType(keyValuePair.getKey(), parseDefinition(keyValuePair.getValue(), "", curRef));
                    objectType.addPatternMember(patternType);
                }

            }
            return objectType;
        }
        if (type.equals("array")) {
            boolean additionalItems = getOptionalMemberAsBool(object, "additionalItems", true);
            ArrayType arrayType = new ArrayType(title, additionalItems);
            JsonObject items = getOptionalMemberAsObject(object, "items");
            if (items == null) {
                return arrayType;
            }
            arrayType.addItem(parseDefinition(items, "item", curRef));
            return arrayType;
        }
        return null;
    }

    private AdditionalProperties parseAdditionalProperties(JsonObject object, String curRef) throws ParseException {
        JsonElement additionalProperties = object.get("additionalProperties");
        if (additionalProperties == null) {
            return new AdditionalProperties(true, new AnyType(""));
        }
        if (additionalProperties.isJsonPrimitive() && ((JsonPrimitive) additionalProperties).isBoolean()) {
            return new AdditionalProperties(additionalProperties.getAsBoolean(), new AnyType(""));
        }
        if (additionalProperties.isJsonObject()) {
            String title = getOptionalMemberAsString(object, "title");
            if (title == null) {
                title = "";
            }
            return new AdditionalProperties(true, parseDefinition(additionalProperties, title, curRef));
        }
        throw new ParseException("additionalProperties is neither boolean nor object");
    }

    private List<String> getOptionalMemberAsStringArray(JsonObject object, String name) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        JsonElement member = object.get(name);
        if (member == null) {
            return result;
        }
        JsonArray memberArray = requireArray(name, member);
        for (JsonElement next : memberArray) {
            String nextString = requireString(name + " ArrayElements", next);
            result.add(nextString);
        }
        return result;
    }


    private JsonObject getRequiredMemberAsObject(JsonObject object, String name) throws ParseException {
        JsonObject member = getOptionalMemberAsObject(object, name);
        if (member == null) {
            throw new ParseException("Required member " + name + " not found");
        }
        return member;
    }


    private JsonObject getOptionalMemberAsObject(JsonObject object, String name) throws ParseException {
        JsonElement member = object.get(name);
        if (member == null) {
            return null;
        }
        return requireObject(name, member);
    }

    private boolean getOptionalMemberAsBool(JsonObject object, String name, boolean fallback) throws ParseException {
        JsonElement member = object.get(name);
        if (member == null) {
            return fallback;
        }
        JsonPrimitive memberPrimitive = requirePrimitive(name, member);
        return requireBool(name, memberPrimitive);
    }


    private Integer getOptionalMemberAsInteger(JsonObject object, String name) throws ParseException {
        JsonElement member = object.get(name);
        if (member == null) {
            return null;
        }
        JsonPrimitive memberPrimitive = requirePrimitive(name, member);
        return requireInt(name, memberPrimitive);
    }

    private String getOptionalMemberAsString(JsonObject object, String name) throws ParseException {
        JsonElement member = object.get(name);
        if (member == null) {
            return null;
        }
        JsonPrimitive memberPrimitive = requirePrimitive(name, member);
        return requireString(name, memberPrimitive);
    }

    private String getRequiredMemberAsString(JsonObject object, String name) throws ParseException {
        JsonElement member = requireMember(object, name);
        JsonPrimitive memberPrimitive = requirePrimitive(name, member);
        return requireString(name, memberPrimitive);
    }

    private JsonElement requireMember(JsonObject object, String name) throws ParseException {
        JsonElement member = object.get(name);
        if (member == null) {
            throw new ParseException("Required member " + name + " not found");
        }
        return member;
    }

    private JsonPrimitive requirePrimitive(String name, JsonElement member) throws ParseException {
        if (!member.isJsonPrimitive()) {
            throw new ParseException("Expected member " + name + " to be a json primitive");
        }
        return member.getAsJsonPrimitive();
    }

    private JsonArray requireArray(String name, JsonElement member) throws ParseException {
        if (!member.isJsonArray()) {
            throw new ParseException("Expected member " + name + " to be a json array");
        }
        return member.getAsJsonArray();
    }

    private JsonObject requireObject(String name, JsonElement member) throws ParseException {
        if (!member.isJsonObject()) {
            throw new ParseException("Expected member " + name + " to be a json object");
        }
        return member.getAsJsonObject();
    }


    private String requireString(String name, JsonElement member) throws ParseException {
        JsonPrimitive memberPrimitive = requirePrimitive(name, member);
        if (!memberPrimitive.isString()) {
            throw new ParseException("Expected member " + name + " to be a string");
        }
        return memberPrimitive.getAsString();
    }

    private int requireInt(String name, JsonPrimitive memberPrimitive) throws ParseException {
        if (!memberPrimitive.isNumber()) {
            throw new ParseException("Expected member " + name + " to be a number");
        }
        return memberPrimitive.getAsInt();
    }

    private boolean requireBool(String name, JsonPrimitive memberPrimitive) throws ParseException {
        if (!memberPrimitive.isBoolean()) {
            throw new ParseException("Expected member " + name + " to be a boolean");
        }
        return memberPrimitive.getAsBoolean();
    }
}
