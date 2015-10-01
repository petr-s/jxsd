package com.github.jxsd.xml;

import com.github.jxsd.xml.annotation.Required;
import com.github.jxsd.xml.exception.InaccessibleClassException;
import com.github.jxsd.xml.exception.RequiredAttributeException;
import com.github.jxsd.xml.exception.UnexpectedElementException;
import com.github.jxsd.xml.reflect.AttributeTemplate;
import com.github.jxsd.xml.reflect.ElementTemplate;
import com.github.jxsd.xml.reflect.Reflect;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XMLReader {
    public synchronized <T> T read(InputStream inputStream, Class<T> clazz) throws IOException {
        final ElementTemplate root = Reflect.elementFromClass(clazz);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            org.xml.sax.XMLReader reader = spf.newSAXParser().getXMLReader();
            SaxHandler<T> handler = new SaxHandler<>(root);
            reader.setContentHandler(handler);
            reader.parse(new InputSource(inputStream));
            return handler.getRoot();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
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

        public SaxHandler(ElementTemplate element) {
            this.element = element;
            stack = new Stack<>();
            text = new StringBuilder();
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

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (stack.isEmpty()) {
                if (!element.getName().equals(localName)) {
                    throw new UnexpectedElementException(localName, locator.getLineNumber(), locator.getColumnNumber());
                }
                root = newInstance(element);
                processAttributes(element, root, attributes);
                stack.push(new StackElement(element, root));
            } else {
                StackElement parent = stack.peek();
                if (!parent.element.hasChild(localName)) {
                    throw new UnexpectedElementException(localName, locator.getLineNumber(), locator.getColumnNumber());
                }
                ElementTemplate childElement = parent.element.getChild(localName);
                Object newChild = newInstance(childElement);
                processAttributes(childElement, newChild, attributes);
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
                    last.element.getValue().set(last.instance, text.toString());
                    text.setLength(0);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            stack.pop();
        }

        private void processAttributes(ElementTemplate element, Object instance, Attributes xmlAttributes) {
            for (Map.Entry<String, AttributeTemplate> entry : element.getAttributes().entrySet()) {
                Object value = xmlAttributes.getValue(entry.getKey());
                if (entry.getValue().hasAnnotation(Required.class) && value == null) {
                    throw new RequiredAttributeException(element.getName(), entry.getKey(), locator.getLineNumber(), locator.getColumnNumber());
                }
                try {
                    entry.getValue().getField().set(instance, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public T getRoot() {
            return root;
        }
    }
}
