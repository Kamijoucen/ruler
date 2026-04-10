# Ruler

轻量级的可嵌入脚本引擎，能解析表达式也能跑脚本。

![ruler shell](pic/shell.gif)

## 用法示例

```java
var cfg = new RulerConfigurationImpl();
var runner = Ruler.compileExpression("1 + 2 * 3", cfg);
System.out.println(runner.run().first().toInteger()); // 7
```

跑脚本：

```java
var script = "var a = 10; var b = 20; return a + b;";
var runner = Ruler.compileScript(script, cfg);
System.out.println(runner.run().first().toInteger()); // 30
```

传外部参数（脚本里用 `$name` 引用）：

```java
var runner = Ruler.compileExpression("$score >= 60", cfg);
var param = new HashMap<String, Object>();
param.put("score", 85);
boolean pass = runner.run(param).first().toBoolean();
```

> `RulerRunner` 编译后可复用，线程安全；`RulerConfiguration` 建议单例。

## 语法速览

### 变量与运算

```ruler
var a = 10;
var b = 3.14;
var c = "hello";
var d = true;
var e = null;
```

运算符：
- `+` `-` `*` `/` `%`：数值运算
- `++`：字符串拼接，如 `"a" ++ "b"` 得 `"ab"`
- `==` `!=` `<>`：非严格比较，字符串和数字会自动转换
- `===` `!==`：严格比较，`1 === '1'` 为 `false`
- `typeof(x)`：类型名，如 `"int"` `"string"` `"array"` `"function"`

### 控制流

```ruler
if a > b {
    println("bigger");
} else if a == b: println("equal");  // 单行简写
else {
    println("smaller");
}

while a < 10 {
    a = a + 1;
    if a == 5 { break; }
}

for item in [1, 2, 3] {
    println(item);
}
```

支持 `break`、`continue`、`return`。

### 函数与闭包

```ruler
fun add(a, b) {
    return a + b;
}

// 箭头简写
var double = fun(x) -> x * 2;

// 闭包
var base = 100;
var calc = fun(x) -> base + x;
println(calc(1)); // 101
```

### 数组与对象

```ruler
var arr = [1, 2, 3];
arr.push(4);
println(arr[1]); // 2
arr[1] = 5;

var obj = {
    name: "ruler",
    age: 18,
    tags: ["script", "java"]
};
println(obj.name);
obj.age = 19;
```

### 字符串插值

```ruler
var name = "Ruler";

// 双引号支持插值
var s1 = "Hello, {name}!";        // Hello, Ruler!
var s2 = "result: {1 + 2}";       // result: 3
var s3 = "obj: { {a:1}.a }";      // obj: 1

// 单引号为原义字符串，不插值
var s4 = 'Hello, {name}!';        // Hello, {name}!

// 多行块字符串（也支持插值）
var s5 = """line1
line2 {name}
line3""";

// 转义
var s6 = "price is \{10}";        // price is {10}
```

### 中缀运算符

```ruler
infix fun push(arr, v) {
    if typeof(arr) != "array": return arr;
    arr.push(v);
    return arr;
}

var arr = [1, 2, 3];
arr push 4 push 5;
println(arr); // [1, 2, 3, 4, 5]
```

### 代理

```ruler
var arr = [1, 2, 3];
var proxyConfig = {
    get: fun(self, name) {
        if name === "myLength" {
            return self.length() + 1;
        }
        return self[name];
    },
    put: fun(self, name, newValue) {
        self[name] = newValue;
    }
};
arr = Proxy(arr, proxyConfig);
println(arr.myLength); // 4
```

### 对象方法与 self 绑定

方法调用会自动把对象绑定到第一个参数 `self`：

```ruler
var obj = {
    name: "ruler",
    getName: fun(self) { return self.name; }
};
println(obj.getName()); // ruler

var fn = obj.getName;
println(fn());          // ruler，self 仍绑定
```

### 规则块

```ruler
rule "score_check" {
    return $score >= 60;
}
rule "name_check" {
    return $name == "ruler";
}
```

执行结果按顺序收集每个 `rule` 的返回值。

### 导入标准库

```ruler
import "/ruler/std/global.txt" op;
import "/ruler/std/collections.txt" collections;
import "/ruler/std/util.txt" util;

println(op.Add(1, 2, 3));
println(collections.Contains(1, [1, 2, 3]));
```

Java 里注册全局导入：

```java
cfg.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
```

## CLI / REPL

```bash
# 交互式 Shell
java -jar ruler.jar

# 执行文件
java -jar ruler.jar -f=script.ruler

# 限制循环次数和栈深
java -jar ruler.jar -f=script.ruler -maxLoopNumber=100000 -maxStackDepth=500
```

## 扩展与集成

- **自定义导入加载器**：实现 `CustomImportLoader` 接口（`match(path)` + `load(path)`），加 `@ImportMatchOrder` 控制优先级，可从 DB、HTTP 等来源加载模块。
- **SPI 扩展点**：实现 `ConfigurationHook`，在 `META-INF/services/com.kamijoucen.ruler.plugin.spi.ConfigurationHook` 注册。内置示例 `StdIoHook` 注入了 `io` 模块，提供 `DeleteFile()`、`WriteNewText()`、`ReadAllText()` 等函数。

## 构建

```bash
mvn -q -DskipTests compile
mvn test
mvn -Dtest=BaseTest#arrayPushTest test
```

## 注意

- 没有 `try/catch`，脚本异常会直接抛 Java 异常，由宿主捕获。
- `RulerConfigurationImpl` 持有导入缓存，需要隔离时每次新建实例。
