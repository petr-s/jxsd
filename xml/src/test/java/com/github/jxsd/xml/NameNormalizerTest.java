package com.github.jxsd.xml;

import org.junit.Test;

import static com.github.jxsd.xml.NameNormalizer.DEFAULT;
import static com.github.jxsd.xml.NameNormalizer.FIRST_LOWER_CASE;
import static org.junit.Assert.assertEquals;

public class NameNormalizerTest {

    @Test
    public void testNameNormalizerDefault() {
        assertEquals("test", DEFAULT.normalize("test"));
        assertEquals("Test", DEFAULT.normalize("Test"));
        assertEquals("TestTest", DEFAULT.normalize("TestTest"));
    }

    @Test
    public void testNameNormalizerFirstLowerCase() {
        assertEquals("test", FIRST_LOWER_CASE.normalize("test"));
        assertEquals("test", FIRST_LOWER_CASE.normalize("Test"));
        assertEquals("testTest", FIRST_LOWER_CASE.normalize("TestTest"));
    }
}