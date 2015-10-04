package com.github.jxsd.xml;

import com.github.jxsd.TestCase;
import com.github.jxsd.xml.annotation.One;
import com.github.jxsd.xml.annotation.Required;
import com.github.jxsd.xml.annotation.Value;
import com.github.jxsd.xml.exception.InaccessibleClassException;
import com.github.jxsd.xml.exception.RequiredAttributeException;
import com.github.jxsd.xml.exception.UnexpectedElementException;
import com.github.jxsd.xml.exception.UnsupportedAttributeType;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class XMLReaderTest extends TestCase {

    @Test(expected = InaccessibleClassException.class)
    public void testReadInaccessibleClass() throws IOException {
        class foo {
        }
        new XMLReader().read(string("<foo></foo>"), foo.class);
    }

    @Test(expected = UnexpectedElementException.class)
    public void testReadInvalidRoot() throws IOException {
        new XMLReader().read(string("<a></a>"), b.class);
    }

    @Test(expected = RequiredAttributeException.class)
    public void testReadRequiredAttribute() throws IOException {
        new XMLReader().read(string("<a></a>"), a.class);
    }

    @Test(expected = UnsupportedAttributeType.class)
    public void testReadUnsupportedAttribute() throws IOException {
        new XMLReader().read(string("<f x=\"asd\" />"), f.class);
    }

    @Test
    public void testRead1NoChild() throws IOException {
        XMLReader reader = new XMLReader();
        b root = reader.read(string("<b></b>"), b.class);
        assertNotNull(root);
        assertNull(root.as);
    }

    @Test
    public void testRead1Child() throws IOException {
        XMLReader reader = new XMLReader();
        b root = reader.read(string("<b><a foo=\"test\"/></b>"), b.class);
        assertNotNull(root);
        assertNotNull(root.as);
        assertEquals(1, root.as.size());
        assertEquals(a.class, root.as.get(0).getClass());
        assertEquals("test", root.as.get(0).foo);
    }

    @Test
    public void testReadValue() throws IOException {
        XMLReader reader = new XMLReader();
        c root = reader.read(string("<c>foo</c>"), c.class);
        assertNotNull(root);
        assertEquals("foo", root.value);
    }

    @Test
    public void testReadMixed() throws IOException {
        XMLReader reader = new XMLReader();
        d root = reader.read(string("<d>foo<e/></d>"), d.class);
        assertNotNull(root);
        assertEquals("foo", root.value);
        assertNotNull(root.es);
        assertEquals(1, root.es.size());
    }

    @Test
    public void testReadValueWhiteSpaces() throws IOException {
        XMLReader reader = new XMLReader();
        c root = reader.read(string("<c>\n\t foo\n</c>"), c.class);
        assertNotNull(root);
        assertEquals("foo", root.value);
    }

    @Test
    public void testReadAttributeTypeShort() throws IOException {
        XMLReader reader = new XMLReader();
        ShortElement root = reader.read(string("<ShortElement a=\"42\" b=\"42\" />"), ShortElement.class);
        assertNotNull(root);
        assertEquals(42, root.a);
        assertEquals(42, root.b.shortValue());
    }

    @Test
    public void testReadAttributeTypeInteger() throws IOException {
        XMLReader reader = new XMLReader();
        IntElement root = reader.read(string("<IntElement a=\"42\" b=\"42\" />"), IntElement.class);
        assertNotNull(root);
        assertEquals(42, root.a);
        assertEquals(42, root.b.intValue());
    }

    @Test
    public void testReadAttributeTypeLong() throws IOException {
        XMLReader reader = new XMLReader();
        LongElement root = reader.read(string("<LongElement a=\"42\" b=\"42\" />"), LongElement.class);
        assertNotNull(root);
        assertEquals(42, root.a);
        assertEquals(42, root.b.longValue());
    }

    @Test
    public void testReadAttributeTypeFloat() throws IOException {
        XMLReader reader = new XMLReader();
        FloatElement root = reader.read(string("<FloatElement a=\"42.42\" b=\"42.42\" />"), FloatElement.class);
        assertNotNull(root);
        assertEquals(42.42, root.a, 0.001);
        assertEquals(42.42, root.b.floatValue(), 0.001);
    }

    @Test
    public void testReadAttributeTypeDouble() throws IOException {
        XMLReader reader = new XMLReader();
        DoubleElement root = reader.read(string("<DoubleElement a=\"42.42\" b=\"42.42\" />"), DoubleElement.class);
        assertNotNull(root);
        assertEquals(42.42, root.a, 0.001);
        assertEquals(42.42, root.b.doubleValue(), 0.001);
    }

    static class a {
        @Required
        String foo;
    }

    static class b {
        @One
        List<a> as;
    }

    static class c {
        @Value
        String value;
    }

    static class e {
    }

    static class d {
        @Value
        String value;
        List<e> es;
    }

    static class f {
        Object x;
    }

    static class ShortElement {
        short a;
        Short b;
    }

    static class IntElement {
        int a;
        Integer b;
    }

    static class LongElement {
        long a;
        Long b;
    }

    static class FloatElement {
        float a;
        Float b;
    }

    static class DoubleElement {
        double a;
        Double b;
    }
}