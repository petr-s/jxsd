package com.github.jxsd.xml.exception;

public class RequiredAttributeException extends RuntimeException {
    public RequiredAttributeException(String elementName, String attributeName, int lineNumber, int columnNumber) {
        super(String.format("ElementTemplate <%s> missing required attribute %s at %d:%d", elementName, attributeName, lineNumber, columnNumber));
    }
}
