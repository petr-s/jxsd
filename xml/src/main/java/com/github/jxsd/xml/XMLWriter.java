package com.github.jxsd.xml;

import com.github.jxsd.xml.reflect.AttributeTemplate;
import com.github.jxsd.xml.reflect.ElementTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static com.github.jxsd.xml.reflect.Reflect.elementFromClass;

public class XMLWriter {
    private static final String EOL = System.lineSeparator();
    private int indent = 2;
    private String encoding;
    private NameNormalizer normalizer = NameNormalizer.DEFAULT;

    public synchronized void setNameNormalizer(NameNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public synchronized <T> void write(OutputStream outputStream, T root) throws IOException {
        write(outputStream, root, true);
    }

    public synchronized <T> void write(OutputStream outputStream, T root, boolean pretty) throws IOException {
        if (outputStream == null || root == null) {
            throw new NullPointerException();
        }
        StringBuilder result = new StringBuilder();
        if (encoding != null) {
            addEncoding(result, pretty);
        }
        try {
            element2xml(elementFromClass(normalizer, root.getClass()), root, result, 0, pretty);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        outputStream.write(result.toString().getBytes());
        outputStream.flush();
    }

    private void addEncoding(StringBuilder result, boolean pretty) {
        result.append("<?xml version=\"1.0\" encoding=\"");
        result.append(encoding);
        result.append("\" ?>");
        if (pretty) {
            result.append(EOL);
        }
    }

    private String indent(int level, int size) {
        return new String(new char[level * size]).replace("\0", " ");
    }

    private void element2xml(ElementTemplate element, Object instance, StringBuilder result, int level, boolean pretty) throws IllegalAccessException {
        Object value = element.getValue() != null ? element.getValue().get(instance) : null;
        if (pretty) {
            result.append(indent(level, indent));
        }
        result.append("<");
        result.append(element.getName());
        for (Map.Entry<String, AttributeTemplate> entry : element.getAttributes().entrySet()) {
            Object attributeValue = entry.getValue().getField().get(instance);
            if (attributeValue != null) {
                result.append(" ");
                result.append(entry.getKey());
                result.append("=\"");
                result.append(attributeValue);
                result.append("\"");
            }
        }
        if (value == null && element.getChildren().size() == 0) {
            result.append(" />");
            return;
        } else {
            result.append(">");
        }

        if (value != null) {
            result.append(value);
        }

        for (ElementTemplate child : element.getChildren()) {
            for (Object item : (List) child.getField().get(instance)) {
                if (pretty) {
                    result.append(EOL);
                }
                element2xml(elementFromClass(normalizer, item.getClass()), item, result, level + 1, pretty);
            }
        }
        if (pretty && element.getChildren().size() > 0) {
            result.append(EOL);
        }

        result.append("</");
        result.append(element.getName());
        result.append(">");
    }
}
