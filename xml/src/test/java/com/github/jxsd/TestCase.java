package com.github.jxsd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestCase {
    protected InputStream file(String resource) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    protected InputStream string(String source) {
        return new ByteArrayInputStream(source.getBytes());
    }
}
