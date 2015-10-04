#JXSD [![Build Status](https://travis-ci.org/petr-s/jxsd.svg)](https://travis-ci.org/petr-s/jxsd) [![Coverage Status](https://coveralls.io/repos/petr-s/jxsd/badge.svg?branch=master&service=github)](https://coveralls.io/github/petr-s/jxsd?branch=master)
##Overview:
Simple, [fast](#benchmark) light-weight framework for generating java classes from .xsd schemas
##X/O/X mapping
###Example:
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

###Benchmark
| Test name | Total time  | Time per single run |
| --- | --- | --- |
|JXSD read|424ms|42ms|
|javax DOM read|505ms|50ms|
|SimpleXML read|818ms|81ms|
```
Java: 1.7.0_51 Oracle Corporation
OS: Windows 7 x86 
CPU: Intel64 Family 6 Model 60 Stepping 3, GenuineIntel
```
