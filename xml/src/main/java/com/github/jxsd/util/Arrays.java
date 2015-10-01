package com.github.jxsd.util;

import java.lang.reflect.Array;
import java.util.List;

public class Arrays {
    private Arrays() {
    }

    public static <T, R> R[] transform(T[] array, Class<R> clazz, UnaryFunction<T, R> function) {
        if (array == null) {
            return null;
        } else if (function == null) {
            throw new NullPointerException();
        }
        @SuppressWarnings("unchecked") R[] result = (R[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < result.length; i++) {
            result[i] = function.call(array[i]);
        }
        return result;
    }

    public static <T, C> T find(List<T> array, C item, BinaryFunction<T, C, Boolean> function) {
        for (T x : array) {
            if (function.call(x, item)) {
                return x;
            }
        }
        return null;
    }

    public static <T, C> boolean contains(List<T> array, C item, BinaryFunction<T, C, Boolean> function) {
        for (T x : array) {
            if (function.call(x, item)) {
                return true;
            }
        }
        return false;
    }

    public interface UnaryFunction<T, R> {
        R call(T input);
    }

    public interface BinaryFunction<T, U, R> {
        R call(T first, U second);
    }
}
