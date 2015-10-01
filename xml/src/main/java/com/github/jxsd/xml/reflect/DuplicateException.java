package com.github.jxsd.xml.reflect;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String parent, String child) {
        super(String.format("%s already contains list of %s", parent, child));
    }
}
