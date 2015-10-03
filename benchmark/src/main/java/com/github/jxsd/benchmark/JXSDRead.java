package com.github.jxsd.benchmark;

import com.github.jxsd.xml.XMLReader;
import com.github.jxsd.xml.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.jxsd.xml.NameNormalizer.FIRST_LOWER_CASE;

public class JXSDRead extends BenchmarkCase {
    private byte[] data;

    @Override
    String getDescription() {
        return "JXSD read";
    }

    @Override
    void prepare() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test.xml");
        data = Files.readAllBytes(Paths.get(resource.toURI()));
    }

    @Override
    public void run() {
        XMLReader reader = new XMLReader();
        reader.setNameNormalizer(FIRST_LOWER_CASE);
        try {
            reader.read(new ByteArrayInputStream(data), Contacts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Contacts {
        List<Person> all;
    }

    public static class Address {
        @Value
        String value;
    }

    public static class Person {
        String name;
        String surname;
        int age;
        List<Address> addresses;
    }
}
