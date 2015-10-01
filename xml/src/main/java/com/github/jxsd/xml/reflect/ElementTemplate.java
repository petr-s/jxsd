package com.github.jxsd.xml.reflect;

import com.github.jxsd.util.Arrays;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElementTemplate {
    String name;
    Class<?> clazz;
    HashMap<String, AttributeTemplate> attributes;
    List<ElementTemplate> children;
    Field value;
    Field field;
    Class<? extends Annotation>[] annotations;

    ElementTemplate(Class<?> clazz, String name, Field field, Annotation[] annotations) {
        this.clazz = clazz;
        this.name = name;
        this.field = field;
        attributes = new HashMap<>();
        children = new ArrayList<>();
        this.annotations = Arrays.transform(annotations, Class.class, new Arrays.UnaryFunction<Annotation, Class>() {
            @Override
            public Class call(Annotation input) {
                return input.annotationType();
            }
        });
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public HashMap<String, AttributeTemplate> getAttributes() {
        return attributes;
    }

    public List<ElementTemplate> getChildren() {
        return children;
    }

    public boolean hasChild(String name) {
        return Arrays.contains(children, name, new Arrays.BinaryFunction<ElementTemplate, String, Boolean>() {
            @Override
            public Boolean call(ElementTemplate element, String name) {
                return element.getName().equals(name);
            }
        });
    }

    public ElementTemplate getChild(String name) {
        return Arrays.find(children, name, new Arrays.BinaryFunction<ElementTemplate, String, Boolean>() {
            @Override
            public Boolean call(ElementTemplate element, String name) {
                return element.getName().equals(name);
            }
        });
    }

    public Field getField() {
        return field;
    }

    public Class<? extends Annotation>[] getAnnotations() {
        return annotations;
    }

    public boolean hasAnnotation(Class<? extends Annotation> clazz) {
        return java.util.Arrays.asList(annotations).contains(clazz);
    }

    public Field getValue() {
        return value;
    }
}