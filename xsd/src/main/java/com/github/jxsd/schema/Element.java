package com.github.jxsd.schema;

import com.github.jxsd.xml.annotation.Required;

public class Element {
    @Required
    protected String name;
    @Required
    protected String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
