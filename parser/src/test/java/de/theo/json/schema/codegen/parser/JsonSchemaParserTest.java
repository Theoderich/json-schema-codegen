package de.theo.json.schema.codegen.parser;

import de.theo.json.schema.codegen.model.AnyType;
import de.theo.json.schema.codegen.model.JsonSchemaDocument;
import de.theo.json.schema.codegen.model.NullType;
import de.theo.json.schema.codegen.model.StringType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonSchemaParserTest {

    private JsonSchemaParser sut = new JsonSchemaParser();

    @Test
    public void testTrueDocument() throws ParseException {
        JsonSchemaDocument result = sut.parse(new ByteArrayInputStream("true".getBytes(StandardCharsets.UTF_8)));
        assertThat(result.getSchema(), is(nullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getTitle(), is(nullValue()));
        assertThat(result.getAllTypes(), hasSize(1));
        assertThat(result.getAllTypes().get(0), is(equalTo(new AnyType("root"))));
    }

    @Test
    public void testFalseDocument() throws ParseException {
        JsonSchemaDocument result = sut.parse(new ByteArrayInputStream("false".getBytes(StandardCharsets.UTF_8)));
        assertThat(result.getSchema(), is(nullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getTitle(), is(nullValue()));
        assertThat(result.getAllTypes(), hasSize(0));
    }



    @Test
    public void testEmptyObjectDocument() throws ParseException {
        JsonSchemaDocument result = sut.parse(new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)));
        assertThat(result.getSchema(), is(nullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getTitle(), is(nullValue()));
        assertThat(result.getAllTypes(), hasSize(1));
        assertThat(result.getAllTypes().get(0), is(equalTo(new AnyType("root"))));
    }

    @Test
    public void testStringDocument() throws ParseException {
        JsonSchemaDocument result = sut.parse(new ByteArrayInputStream("{ \"type\": \"string\" }".getBytes(StandardCharsets.UTF_8)));
        assertThat(result.getSchema(), is(nullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getTitle(), is(nullValue()));
        assertThat(result.getAllTypes(), hasSize(1));
        assertThat(result.getAllTypes().get(0), is(equalTo(new StringType("root"))));
    }

    @Test
    public void testSimpleFullDocument() throws ParseException {
        JsonSchemaDocument result = sut.parse(new ByteArrayInputStream(("{" +
                "\"$schema\": \"http://json-schema.org/schema#\",\n" +
                "\"$id\": \"http://test.com/schemas/testschema.json\",\n" +
                "\"title\": \"TestSchema\",\n" +
                "\"type\": \"string\" }").getBytes(StandardCharsets.UTF_8)));
        assertThat(result.getSchema(), is(equalTo("http://json-schema.org/schema#")));
        assertThat(result.getId(), is(equalTo("http://test.com/schemas/testschema.json")));
        assertThat(result.getTitle(), is("TestSchema"));
        assertThat(result.getAllTypes(), hasSize(1));
        assertThat(result.getAllTypes().get(0), is(equalTo(new StringType("TestSchema"))));
    }
}