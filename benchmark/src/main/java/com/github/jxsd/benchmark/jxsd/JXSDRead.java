package com.github.jxsd.benchmark.jxsd;

import com.github.jxsd.benchmark.BenchmarkCase;
import com.github.jxsd.xml.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.jxsd.xml.NameNormalizer.FIRST_LOWER_CASE;

public class JXSDRead extends BenchmarkCase {
    private byte[] data;

    @Override
    public String getDescription() {
        return "JXSD read";
    }

    @Override
    public void prepare() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test.xml");
        data = Files.readAllBytes(Paths.get(resource.toURI()));
    }

    @Override
    public void run() {
        XMLReader reader = new XMLReader();
        reader.setNameNormalizer(FIRST_LOWER_CASE);
        try {
            Contacts contacts = reader.read(new ByteArrayInputStream(data), Contacts.class);
            int a = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
