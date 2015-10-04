package com.github.jxsd.benchmark.dom;

import com.github.jxsd.benchmark.BenchmarkCase;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DOMRead extends BenchmarkCase {
    private byte[] data;

    @Override
    public String getDescription() {
        return "javax DOM read";
    }

    @Override
    public void prepare() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test.xml");
        data = Files.readAllBytes(Paths.get(resource.toURI()));
    }

    private Node findChildNode(Node parent, String name) {
        NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeName().equals(name)) {
                return childNodes.item(i);
            }
        }
        return null;
    }

    @Override
    public void run() {
        Contacts contacts = new Contacts();
        contacts.all = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(data));
            Element root = document.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item.getNodeName().equals("person")) {
                    NamedNodeMap attributes = item.getAttributes();
                    Person person = new Person();
                    person.name = attributes.getNamedItem("name").getNodeValue();
                    person.surname = attributes.getNamedItem("surname").getNodeValue();
                    person.age = Integer.parseInt(attributes.getNamedItem("age").getNodeValue());
                    person.address = findChildNode(item, "address").getTextContent();
                    contacts.all.add(person);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
