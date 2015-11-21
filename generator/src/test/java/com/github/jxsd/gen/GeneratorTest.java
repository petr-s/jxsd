package com.github.jxsd.gen;

import com.github.jxsd.TestCase;
import com.github.jxsd.schema.Schema;
import com.github.jxsd.xml.NameNormalizer;
import com.github.jxsd.xml.XMLReader;
import org.junit.Test;

public class GeneratorTest extends TestCase {

    @Test
    public void testGenerate() throws Exception {
        XMLReader reader = new XMLReader();
        reader.setNameNormalizer(NameNormalizer.FIRST_LOWER_CASE);
        Schema schema = reader.read(file("sample_w3.xsd"), Schema.class);
        Generator generator = new Generator();
        System.out.println(generator.generate(schema, ""));
    }
}