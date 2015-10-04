package com.github.jxsd.benchmark.simple;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Person {
    @Attribute
    String name;
    @Attribute
    String surname;
    @Attribute
    int age;
    @Element
    String address;
}
