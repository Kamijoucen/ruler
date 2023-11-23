# 简单可扩展的规则引擎


## hello world
```java
Ruler.compileExpression("println('hello world!')", new RulerConfigurationImpl()).run();
```


```java
// 拼接脚本
StringBuffer buffer = new StringBuffer();
buffer.append("var text = 'hello world!';");
buffer.append("println(text);");
// configuration可以控制规则引擎的运行行为，通常将RulerConfiguration做成一个单例对象
RulerConfiguration configuration = new RulerConfigurationImpl();
// runner是可复用并且线程安全的，尽可能将runner缓存起来，因为执行complie有较大开销
RuleScript runner = Ruler.compileScript(buffer.toString(), configuration);
// 每次run都会产生新的执行上下文，因此runner.run线程安全
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

## 自定义中缀表达式

```javascript

// 使用infix关键字定义名为 push 的中缀运算
infix fun push(array, right) {
    if typeof(array) != 'array': return array;
    array.push(right);
    return array;
}

var arr = [1, 2, 3];

// 
arr push 4 push 5;

// 此时arr的值为 [1, 2, 3, 4, 5]
println(arr);

```

## 代理

```javascript



```


# 未来更新计划（优先级排序）
## 模式匹配
## 分号插补
## 不可变数据类型
## 性能优化
## 优化报错信息（打印堆栈，行号等等）
## 内置标准库（网络，json，web，安全等）