package com.github.jxsd.xml.reflect;

import com.github.jxsd.xml.annotation.Required;
import com.github.jxsd.xml.annotation.Value;
import com.github.jxsd.xml.exception.ValueAlreadyDefinedException;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.github.jxsd.xml.reflect.Reflect.elementFromClass;
import static org.junit.Assert.*;

public class ReflectTest {

    @Test(expected = NullPointerException.class)
    public void testElementFromClassNull() throws Exception {
        elementFromClass(null);
    }

    @Test
    public void testElementFromClassEmpty() {
        class a {
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.attributes.size());
        assertEquals(0, element.children.size());
        assertNull(element.getValue());
    }

    @Test
    public void testElementFromClassEmptyHasValue() {
        class a {
            @Value
            String value;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.attributes.size());
        assertEquals(0, element.children.size());
        assertNotNull(element.getValue());
    }

    @Test(expected = ValueAlreadyDefinedException.class)
    public void testElementFromClass2Values() {
        class a {
            @Value
            String value;
            @Value
            String value2;
        }
        elementFromClass(a.class);
    }

    @Test
    public void testElementFromClassSingleAttribute() {
        class a {
            String name;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.children.size());
        assertEquals(1, element.attributes.size());
        assertTrue(element.attributes.containsKey("name"));
        assertEquals(String.class, element.attributes.get("name").clazz);
        assertNull(element.getValue());
    }

    @Test
    public void testElementFromClassSingleAttributeAnnotation() {
        class a {
            @Required
            String name;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.children.size());
        assertEquals(1, element.attributes.size());
        assertTrue(element.attributes.containsKey("name"));
        assertEquals(String.class, element.attributes.get("name").clazz);
        assertNotNull(element.attributes.get("name").annotations);
        assertEquals(1, element.attributes.get("name").annotations.length);
        assertEquals(Required.class, element.attributes.get("name").annotations[0]);
        assertNull(element.getValue());
    }

    @Test
    public void testElementFromClassSingleAttributeTransient() {
        class a {
            String name;
            transient String foo;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.children.size());
        assertEquals(1, element.attributes.size());
        assertTrue(element.attributes.containsKey("name"));
        assertEquals(String.class, element.attributes.get("name").clazz);
        assertNull(element.getValue());
    }

    @Test
    public void testElementFromClassMultipleAttributes() {
        class a {
            String name;
            int age;
            Date born;
            Float weight;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.children.size());
        assertEquals(4, element.attributes.size());
        assertTrue(element.attributes.containsKey("name"));
        assertEquals(String.class, element.attributes.get("name").clazz);
        assertTrue(element.attributes.containsKey("age"));
        assertEquals(int.class, element.attributes.get("age").clazz);
        assertTrue(element.attributes.containsKey("born"));
        assertEquals(Date.class, element.attributes.get("born").clazz);
        assertTrue(element.attributes.containsKey("weight"));
        assertEquals(Float.class, element.attributes.get("weight").clazz);
        assertNull(element.getValue());
    }

    @Test
    public void testElementFromClassChild() {
        class b {
        }
        class a {
            List<b> bs;
        }
        ElementTemplate element = elementFromClass(a.class);
        assertEquals(a.class, element.clazz);
        assertEquals("a", element.name);
        assertEquals(0, element.attributes.size());
        assertEquals(1, element.children.size());
        assertNull(element.getValue());
        ElementTemplate child = element.children.get(0);
        assertEquals(b.class, child.clazz);
        assertEquals("b", child.name);
        assertEquals(0, child.attributes.size());
        assertEquals(0, child.children.size());
        assertNull(element.getValue());
    }

    @Test(expected = DuplicateException.class)
    public void testElementFromClassChildDuplicate() {
        class b {
        }
        class a {
            List<b> bs;
            List<b> bs2;
        }
        elementFromClass(a.class);
    }
}