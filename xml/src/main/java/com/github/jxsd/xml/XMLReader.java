package com.github.jxsd.xml;

import com.github.jxsd.xml.annotation.Required;
import com.github.jxsd.xml.exception.InaccessibleClassException;
import com.github.jxsd.xml.exception.RequiredAttributeException;
import com.github.jxsd.xml.exception.UnexpectedElementException;
import com.github.jxsd.xml.exception.UnsupportedAttributeType;
import com.github.jxsd.xml.reflect.AttributeTemplate;
import com.github.jxsd.xml.reflect.ElementTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.github.jxsd.xml.reflect.Reflect.elementFromClass;

public class XMLReader {
    private NameNormalizer normalizer = NameNormalizer.DEFAULT;
    private BreakHandler breakHandler = new BreakHandler() {
        @Override
        public boolean onElement(Object element) {
            return false;
        }
    };

    public synchronized void setNameNormalizer(NameNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    public synchronized <T> T read(InputStream inputStream, Class<T> clazz) throws IOException {
        final ElementTemplate root = elementFromClass(normalizer, clazz);
        SaxHandler<T> handler = new SaxHandler<>(root);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            org.xml.sax.XMLReader reader = spf.newSAXParser().getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(inputStream));
            return handler.getRoot();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            if (e instanceof BreakException) {
                return handler.getRoot();
            }
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void setBreakHandler(BreakHandler breakHandler) {
        this.breakHandler = breakHandler;
    }

    public interface BreakHandler {
        boolean onElement(Object element);
    }

    private class BreakException extends SAXException {

    }

    private class StackElement {
        ElementTemplate element;
        Object instance;

        public StackElement(ElementTemplate element, Object instance) {
            this.element = element;
            this.instance = instance;
        }
    }

    private class SaxHandler<T> extends DefaultHandler {
        ElementTemplate element;
        T root;
        Locator locator;
        Stack<StackElement> stack;
        StringBuilder text;
        Set<String> prefixes;

        public SaxHandler(ElementTemplate element) {
            this.element = element;
            stack = new Stack<>();
            text = new StringBuilder();
            prefixes = new HashSet<>();
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            prefixes.add(prefix);
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            prefixes.remove(prefix);
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        <E> E newInstance(ElementTemplate element) {
            try {
                return (E) element.getClazz().newInstance();
            } catch (InstantiationException e) {
                if (e.getMessage().contains("$")) {
                    throw new InaccessibleClassException(e.getMessage().split("\\$")[1]);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String stripPrefix(String value) {
            for (String prefix : prefixes) {
                if (value.startsWith(prefix)) {
                    return value.substring(prefix.length() + 1);
                }
            }
            return value;
        }

        private HashMap<String, String> normalizeAttributes(Attributes attributes) {
            HashMap<String, String> normalizedAttributes = new HashMap<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                normalizedAttributes.put(normalizer.normalize(attributes.getLocalName(i)),
                        stripPrefix(attributes.getValue(i)));
            }
            return normalizedAttributes;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            localName = normalizer.normalize(localName);
            HashMap<String, String> normalizedAttributes = normalizeAttributes(attributes);
            if (stack.isEmpty()) {
                if (!element.getName().equals(localName)) {
                    throw new UnexpectedElementException(localName, locator.getLineNumber(), locator.getColumnNumber());
                }
                root = newInstance(element);
                processAttributes(element, root, normalizedAttributes);
                if (breakHandler.onElement(root)) {
                    throw new BreakException();
                }
                stack.push(new StackElement(element, root));
            } else {
                StackElement parent = stack.peek();
                if (!parent.element.hasChild(localName)) {
                    throw new UnexpectedElementException(localName, locator.getLineNumber(), locator.getColumnNumber());
                }
                ElementTemplate childElement = parent.element.getChild(localName);
                Object newChild = newInstance(childElement);
                processAttributes(childElement, newChild, normalizedAttributes);
                try {
                    List list = (List) childElement.getField().get(parent.instance);
                    if (list == null) {
                        list = new ArrayList<>();
                        childElement.getField().set(parent.instance, list);
                    }
                    list.add(newChild);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (breakHandler.onElement(newChild)) {
                    throw new BreakException();
                }
                stack.push(new StackElement(childElement, newChild));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            StackElement last = stack.peek();
            if (last.element.getValue() != null) {
                try {
                    String value = text.toString().replaceAll("\\s", "");
                    last.element.getValue().set(last.instance, parseTypedValue(last.element.getValue().getType(), value));
                    text.setLength(0);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            stack.pop();
        }

        private Object parseTypedValue(Class<?> clazz, String value) {
            if (clazz.equals(String.class)) {
                return value;
            } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                return Short.valueOf(value);
            } else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                return Integer.valueOf(value);
            } else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
                return Long.valueOf(value);
            } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                return Float.valueOf(value);
            } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                return Double.valueOf(value);
            } else if (clazz.isEnum()) {
                return Enum.valueOf(clazz.asSubclass(Enum.class), value);
            }
            return null;
        }

        private void processAttributes(ElementTemplate element, Object instance, HashMap<String, String> attributes) {
            for (Map.Entry<String, AttributeTemplate> entry : element.getAttributes().entrySet()) {
                String value = attributes.get(entry.getKey());
                if (entry.getValue().hasAnnotation(Required.class) && value == null) {
                    throw new RequiredAttributeException(element.getName(), entry.getKey(), locator.getLineNumber(), locator.getColumnNumber());
                }
                try {
                    Class<?> clazz = entry.getValue().getField().getType();
                    Object typedValue = parseTypedValue(clazz, value);
                    if (typedValue == null) {
                        throw new UnsupportedAttributeType(element.getName(), clazz, locator.getLineNumber(), locator.getColumnNumber());
                    }
                    entry.getValue().getField().set(instance, typedValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized T getRoot() {
            return root;
        }
    }
}
