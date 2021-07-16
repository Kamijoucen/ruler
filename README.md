# 简单可扩展的规则引擎
```java
StringBuffer buffer = new StringBuffer();

buffer.append("var text = 'hello world!'; ");
buffer.append("println(text);         ");

RuleScript runner = Ruler.compile(buffer.toString());

runner.run();
```
## 运行结果

> hello world!