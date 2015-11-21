package com.github.jxsd.gen;

import com.github.jxsd.schema.*;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;

public class Generator {
    private <T> HashMap<String, T> named(List<T> list, GetName<T> getName) {
        HashMap<String, T> map = new HashMap<>();
        for (T item : list) {
            map.put(getName.getName(item), item);
        }
        return map;
    }

    public String generate(Schema schema, String pkg) {
        HashMap<String, SimpleType> simpleTypes = named(schema.getSimpleTypes(), new GetName<SimpleType>() {

            @Override
            public String getName(SimpleType object) {
                return object.getName();
            }
        });
        HashMap<String, ComplexType> complexTypes = named(schema.getComplexTypes(), new GetName<ComplexType>() {

            @Override
            public String getName(ComplexType object) {
                return object.getName();
            }
        });
        for (Element element : schema.getElements()) {
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(element.getName()).addModifiers(Modifier.PUBLIC);
            String type = element.getType();
            ComplexType complexType = complexTypes.get(type);// TODO: add validation
            for (Attribute attribute : complexType.getAttributes()) {
                SimpleType simpleType = simpleTypes.get(attribute.getType());
                classBuilder.addField(String.class, attribute.getName(), Modifier.PRIVATE);
            }
            JavaFile javaFile = JavaFile.builder("asd.asd", classBuilder.build()).build();
            return javaFile.toString();
        }
        return null;
    }

    private interface GetName<T> {
        String getName(T object);
    }
}
