package com.github.jxsd.xml.reflect;

import com.github.jxsd.util.Arrays;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AttributeTemplate {
    Class<?> clazz;
    Field field;
    Class<? extends Annotation>[] annotations;

    public AttributeTemplate(Class<?> clazz, Field field, Annotation[] annotations) {
        this.clazz = clazz;
        this.field = field;
        this.annotations = Arrays.transform(annotations, Class.class, new Arrays.UnaryFunction<Annotation, Class>() {
            @Override
            public Class call(Annotation input) {
                return input.annotationType();
            }
        });
    }

    public Class<?> getClazz() {
        return clazz;
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
}
