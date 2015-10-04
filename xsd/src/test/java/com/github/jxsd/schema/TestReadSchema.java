package com.github.jxsd.schema;

import com.github.jxsd.TestCase;
import com.github.jxsd.xml.NameNormalizer;
import com.github.jxsd.xml.XMLReader;
import org.junit.Test;

import java.io.IOException;

public class TestReadSchema extends TestCase {
    @Test
    public void testSampleW3() throws IOException {
        XMLReader reader = new XMLReader();
        reader.setNameNormalizer(NameNormalizer.FIRST_LOWER_CASE);
        Schema schema = reader.read(file("sample_w3.xsd"), Schema.class);
        int a = 0;
    }
}
