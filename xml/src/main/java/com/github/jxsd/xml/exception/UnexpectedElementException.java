package com.github.jxsd.xml.exception;

public class UnexpectedElementException extends RuntimeException {
    public UnexpectedElementException(String name, int lineNumber, int columnNumber) {
        super(String.format("Unexpected element <%s> at %d:%d", name, lineNumber, columnNumber));
    }
}
