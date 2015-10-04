package com.github.jxsd.benchmark.simple;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Contacts {
    @ElementList(inline = true)
    List<Person> person;
}
