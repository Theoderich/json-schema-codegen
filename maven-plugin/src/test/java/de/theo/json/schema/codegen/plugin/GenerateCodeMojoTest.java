package de.theo.json.schema.codegen.plugin;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class GenerateCodeMojoTest extends AbstractMojoTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testExecute() throws Exception {

        File testPom = new File(getBasedir(), "src/test/resources/test-pom.xml");

        GenerateCodeMojo lookup = (GenerateCodeMojo) lookupMojo("generate-code", testPom);
        assertNotNull(lookup);

        lookup.execute();

        File outputPerson = new File(getBasedir(), "target/test/generated-sources/de/qaware/test/Person.java");
        assertTrue("Output should exist", outputPerson.exists());

        File outputPet = new File(getBasedir(), "target/test/generated-sources/de/qaware/test/Pet.java");
        assertTrue("Output should exist", outputPet.exists());

    }
}