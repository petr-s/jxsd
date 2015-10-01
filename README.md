#JXSD [![Build Status](https://travis-ci.org/petr-s/jxsd.svg)](https://travis-ci.org/petr-s/jxsd) [![Coverage Status](https://coveralls.io/repos/petr-s/jxsd/badge.svg?branch=master&service=github)](https://coveralls.io/github/petr-s/jxsd?branch=master)
##Overview:
Simple light-weight framework for generating java classes from .xsd schemas
##Example mapping:
```xml
<b attribute="hello" another="world">
    <a>test</a>
</b>
```

```java
class A {
    @Value
    String value;
}

class B {
    String attribute;
    String another;
    List<A> children;
}
```
