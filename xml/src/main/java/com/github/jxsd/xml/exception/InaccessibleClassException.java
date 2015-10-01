package com.github.jxsd.xml.exception;

public class InaccessibleClassException extends RuntimeException {
    public InaccessibleClassException(String clazz) {
        super(String.format("%s class is inaccessible (use static?)", clazz));
    }
}
