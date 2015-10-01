package com.github.jxsd.util;

import org.junit.Test;

import static com.github.jxsd.util.Arrays.transform;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class ArraysTest {

    @Test
    public void testTransformNull() {
        assertNull(transform(null, null, null));
    }

    @Test
    public void testTransformSimple() {
        class A {
            int x;

            public A(int x) {
                this.x = x;
            }
        }
        A[] input = new A[]{new A(0), new A(1), new A(2)};
        Integer[] expected = {0, 1, 2};
        assertArrayEquals(expected, transform(input, Integer.class, new Arrays.UnaryFunction<A, Integer>() {
            @Override
            public Integer call(A input) {
                return input.x;
            }
        }));
    }
}