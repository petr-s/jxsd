package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class SimpleType {
    @Required
    private String name;
    private List<Restriction> restrictions = new ArrayList<>();
}
