package com.github.jxsd.xml;

public abstract class NameNormalizer {
    public static final NameNormalizer DEFAULT = new NameNormalizer() {
        @Override
        public String normalize(String name) {
            return name;
        }
    };

    public static final NameNormalizer FIRST_LOWER_CASE = new NameNormalizer() {
        @Override
        public String normalize(String name) {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
    };

    public abstract String normalize(String name);
}
