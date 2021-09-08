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

# 语法说明
## 闭包
```javascript
var b = 100;
// 函数可以作为值传递
var c = fun(a) {
    return a + b;
};
println(c(1) + b); // 打印 101
```
## 控制语句
```javascript
var a = 1;
var b = 10;

if a >= b {
    println("a!!!!");
} else {
    println("b!!!!");
}

while a < b {
    println("loop!", a);
    a = a + 1;
}
```
## 获取引擎外部的值
```javascript
var a = 1;
var b = $out; // $表示从外部取值
println(a + b);
```