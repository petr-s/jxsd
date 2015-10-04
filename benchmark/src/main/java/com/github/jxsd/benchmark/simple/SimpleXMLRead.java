package com.github.jxsd.benchmark.simple;

import com.github.jxsd.benchmark.BenchmarkCase;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleXMLRead extends BenchmarkCase {
    private byte[] data;

    @Override
    public String getDescription() {
        return "SimpleXML read";
    }

    @Override
    public void prepare() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test.xml");
        data = Files.readAllBytes(Paths.get(resource.toURI()));
    }

    @Override
    public void run() {
        Serializer serializer = new Persister();
        try {
            serializer.read(Contacts.class, new ByteArrayInputStream(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
