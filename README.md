# 简单可扩展的规则引擎
```java
// 拼接脚本
StringBuffer buffer = new StringBuffer();
buffer.append("var text = 'hello world!';");
buffer.append("println(text);");
// configuration可以控制规则引擎的运行行为，通常将RulerConfiguration做成一个单例对象
RulerConfiguration configuration = new RulerConfigurationImpl();
// runner是可复用并且线程安全的，尽可能将runner缓存起来，因为执行complie有较大开销
RuleScript runner = Ruler.compileScript(buffer.toString(), configuration);
// 每次润run都会产生新的执行上下文，因此runner.run线程安全
runner.run();
// 运行结果打印 hello world!
```
```java
RulerConfiguration configuration = new RulerConfigurationImpl();
RuleScript runner = Ruler.compileExpression("$name == '张三'");
// 用户自定义的参数，key就是表达式中的外部变量（$开头的变量）
Map<String, Object> args = new HashMap<String, Object>();
args.put("name", "李四");
// 运行规则，并返回运行结果
RuleResult result = runner.run(args);
// 打印规则执行结果
System.out.println(result.first().toBoolean());
// 打印 false
```

# 语法说明
## 闭包
```javascript
var b = 100;
// 函数可以作为值传递
var c = fun(a) {
    return a + b;
};
println(c(1)); // 打印 101
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