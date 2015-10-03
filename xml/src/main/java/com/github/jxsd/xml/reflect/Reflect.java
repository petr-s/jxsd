package com.github.jxsd.xml.reflect;

import com.github.jxsd.xml.NameNormalizer;
import com.github.jxsd.xml.annotation.Value;
import com.github.jxsd.xml.exception.ValueAlreadyDefinedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.github.jxsd.xml.NameNormalizer.DEFAULT;

public class Reflect {

    private static String className(NameNormalizer normalizer, Class<?> clz) {
        return normalizer.normalize(clz.getSimpleName());
    }

    public static ElementTemplate elementFromClass(Class<?> clazz) {
        return elementFromClass(DEFAULT, clazz, null, null);
    }

    public static ElementTemplate elementFromClass(NameNormalizer normalizer, Class<?> clazz) {
        return elementFromClass(normalizer, clazz, null, null);
    }

    public static ElementTemplate elementFromClass(Class<?> clazz, Field f, Annotation[] annotations) {
        return elementFromClass(DEFAULT, clazz, f, annotations);
    }

    public static ElementTemplate elementFromClass(NameNormalizer normalizer, Class<?> clazz, Field f, Annotation[] annotations) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        ElementTemplate element = new ElementTemplate(clazz, className(normalizer, clazz), f, annotations);
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().contains("$") || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            if (field.getAnnotation(Value.class) != null) {
                if (element.value != null) {
                    throw new ValueAlreadyDefinedException();
                }
                element.value = field;
            } else if (field.getType().equals(List.class)) {
                Class<?> type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                String name = className(normalizer, type);
                for (ElementTemplate child : element.children) {
                    if (name.equals(child.name)) {
                        throw new DuplicateException(element.name, name);
                    }
                }
                element.children.add(elementFromClass(normalizer, type, field, field.getAnnotations()));
            } else {
                element.attributes.put(normalizer.normalize(field.getName()), new AttributeTemplate(field.getType(), field, field.getAnnotations()));
            }
        }
        return element;
    }
}
