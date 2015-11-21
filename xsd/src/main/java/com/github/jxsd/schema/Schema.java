package com.github.jxsd.schema;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    private List<SimpleType> simpleTypes = new ArrayList<>();
    private List<ComplexType> complexTypes = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public List<SimpleType> getSimpleTypes() {
        return simpleTypes;
    }

    public List<ComplexType> getComplexTypes() {
        return complexTypes;
    }

    public List<Element> getElements() {
        return elements;
    }
}
