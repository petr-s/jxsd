package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.One;
import com.github.jxsd.xml.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class SimpleType {
    @Required
    private String name;
    @One
    private List<Restriction> restrictions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Restriction getRestriction() {
        return restrictions.get(0);
    }
}
