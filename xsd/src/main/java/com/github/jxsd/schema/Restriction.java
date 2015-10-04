package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class Restriction {
    @Required
    private String base;
    private List<Pattern> patterns = new ArrayList<>();
}
