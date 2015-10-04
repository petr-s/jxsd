package com.github.jxsd.xml.exception;

public class UnsupportedAttributeType extends RuntimeException {
    public UnsupportedAttributeType(String name, Class<?> clazz, int lineNumber, int columnNumber) {
        super(String.format("%s of %s at %d:%d", name, clazz, lineNumber, columnNumber));
    }
}
