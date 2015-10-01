package com.github.jxsd.xml;

import com.github.jxsd.xml.annotation.Value;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XMLWriterTest {

    private static final String EOL = System.lineSeparator();

    @Test(expected = NullPointerException.class)
    public void testWriteNull() throws IOException {
        new XMLWriter().write(null, null);
    }

    @Test
    public void testWriteA() throws IOException {
        a a = new a();
        a.attr1 = "test";
        a.value = "foo";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new XMLWriter().write(outputStream, a);
        assertEquals("<a attr1=\"test\">foo</a>", outputStream.toString());
    }

    @Test
    public void testWriteAEncodingNotPretty() throws IOException {
        a a = new a();
        a.attr1 = "test";
        a.value = "foo";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter();
        writer.setEncoding("UTF-8");
        writer.write(outputStream, a, false);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<a attr1=\"test\">foo</a>";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testWriteAEncodingPretty() throws IOException {
        a a = new a();
        a.attr1 = "test";
        a.value = "foo";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter();
        writer.setEncoding("UTF-8");
        writer.write(outputStream, a);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + EOL +
                "<a attr1=\"test\">foo</a>";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testWriteChildrenEncodingPretty() throws IOException {
        c c = new c();
        c.add(new b("test"));
        c.add(new b("test 2"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter();
        writer.setEncoding("UTF-8");
        writer.write(outputStream, c);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + EOL +
                "<c>" + EOL +
                "  <b>test</b>" + EOL +
                "  <b>test 2</b>" + EOL +
                "</c>";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testWriteChildrenEncodingNotPretty() throws IOException {
        c c = new c();
        c.add(new b("test"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter();
        writer.setEncoding("UTF-8");
        writer.write(outputStream, c, false);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<c>" +
                "<b>test</b>" +
                "</c>";
        assertEquals(expected, outputStream.toString());
    }

    static class a {
        String attr1;
        @Value
        String value;
    }

    static class b {
        @Value
        String value;

        public b(String value) {
            this.value = value;
        }
    }

    static class c {
        List<b> children;

        public c() {
            children = new ArrayList<>();
        }

        void add(b child) {
            children.add(child);
        }
    }
}