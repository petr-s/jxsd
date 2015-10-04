package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.Required;

public class Attribute {
    @Required
    protected String name;
    @Required
    protected String type;
}
