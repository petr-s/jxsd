package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class ComplexType {
    @Required
    private String name;
    private List<Sequence> sequences = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
