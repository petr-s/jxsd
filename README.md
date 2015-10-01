#JXSD
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