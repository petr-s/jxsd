package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.One;
import com.github.jxsd.xml.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class Restriction {
    @Required
    private String base;
    @One
    private List<Pattern> patterns = new ArrayList<>();

    public String getBase() {
        return base;
    }

    public Pattern getPattern() {
        return patterns.get(0);
    }
}
