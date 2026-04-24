# Ruler

Ruler 是一款轻量级、可嵌入的脚本引擎，适用于 Java 宿主环境。提供动态类型语法、闭包、数组与对象操作、模式匹配、中缀运算符扩展、代理、规则块等特性。无外部依赖，单 jar 即可嵌入。

## 核心特性

- 动态类型，编译期对字面量做静态类型检查
- 函数/闭包/箭头简写，省略 return 时最后一条语句值自动返回
- 数组与对象（RSON）字面量、索引访问、方法调用、self 自动绑定
- 字符串插值（双引号）、原义字符串（单引号）、多行块字符串
- 模式匹配：字面量、变量绑定、通配符、数组解构、对象解构、守卫表达式
- 自定义中缀运算符
- Proxy 代理拦截 get/set
- 规则块（Rule）按顺序收集返回值
- 模块导入，支持自定义加载器与 SPI 扩展
- CLI / REPL 交互式 Shell

## 快速开始

```java
var cfg = new RulerConfigurationImpl();

// 单行表达式
var runner = Ruler.compile("1 + 2 * 3", cfg);
System.out.println(runner.run().first().toInteger()); // 7

// 多行脚本
var script = "var a = 10\nvar b = 20\nreturn a + b";
var runner = Ruler.compile(script, cfg);
System.out.println(runner.run().first().toInteger()); // 30

// 外部参数
var runner2 = Ruler.compile("$score >= 60", cfg);
var param = new HashMap<String, Object>();
param.put("score", 85);
boolean pass = runner2.run(param).first().toBoolean();
```

> `RulerRunner` 编译后可复用且线程安全，`RulerConfiguration` 建议作为进程级单例。

## 完整特性演示

以下单脚本展示了 Ruler 的所有主要语法特性：

```ruler
// ===== 变量与运算 =====
var x = 10
var y = 3.14
var s = "hello"
var flag = true
var empty = null

var sum = x + y * 2          // 数值运算
var msg = s ++ " world"      // 字符串拼接

// ===== 数组与对象 =====
var arr = [1, 2, 3]
arr.push(4)
arr[1] = 5

var obj = {
    name: "ruler",
    age: 18,
    tags: ["script", "java"],
    greet: fun(self) {
        "hi, " ++ self.name
    }
}
obj.age = 19
var greetFn = obj.greet      // self 自动绑定

// ===== 字符串插值 =====
var name = "Ruler"
var s1 = "Hello, {name}!"
var s2 = 'raw: {name}'       // 单引号不插值
var s3 = """multi
{name}"""

// ===== 控制流 =====
var n = 5
if n > 3 {
    n = n + 1
} else if n == 3: n = n      // 单行简写
else {
    n = 0
}

var i = 0
while i < 3 {
    i = i + 1
    if i == 2 { break }
}

for item in [10, 20, 30] {
    i = i + item
}

// ===== 函数与闭包 =====
fun add(a, b) {
    a + b                     // 省略 return
}

var double = fun(x) -> x * 2

var base = 100
var calc = fun(x) -> base + x

// ===== 模式匹配（含结构匹配）=====
var result = match [1, 2, {a: 3, b: 4}] {
    [1, 2, {a: var x, b: var y}] -> x + y
    [var head, ...var tail] -> head + tail.length()
    _ -> 0
}

var guardResult = match 15 {
    var n if n > 10 -> "big"
    var n if n > 0  -> "small"
    _ -> "zero"
}

// ===== 中缀运算符 =====
infix fun push(arr, v) {
    arr.push(v)
    arr
}
var pipeline = [1, 2] push 3 push 4

// ===== 代理 =====
var proxyConfig = {
    get: fun(self, key) {
        if key === "doubleLength" {
            return self.length() * 2
        }
        self[key]
    }
}
var proxied = Proxy(pipeline, proxyConfig)

// ===== 规则块 =====
rule "check_score" {
    $score >= 60
}
rule "check_name" {
    $name === "ruler"
}

// ===== 导入与 typeof =====
// import "/ruler/std/collections.txt" collections
var t = typeof(arr)            // "array"

// ===== 综合返回 =====
{
    sum: sum,
    msg: msg,
    arrLen: arr.length(),
    objName: obj.name,
    greet: greetFn(),
    interpolated: s1,
    loopResult: i,
    closureResult: calc(1),
    matchResult: result,
    guardResult: guardResult,
    pipeline: pipeline,
    proxyValue: proxied.doubleLength,
    typeName: t
}
```

## 模式匹配语法说明

```ruler
match value {
    1           -> "one"           // 字面量
    "hello"     -> "string"       // 字符串字面量
    var n       -> n + 1          // 变量绑定
    TOKEN_TYPE  -> "matched"      // 值比较（使用已定义变量的值）
    _           -> "default"      // 通配符
    [var a, var b]      -> a + b          // 数组解构
    [var h, ...var t]   -> h              // 数组 + rest
    {x: var v}      -> v                  // 对象解构（部分匹配）
    {a: var x, ...var r}-> x + r.b       // 对象 + rest
    var n if n > 0 -> "positive"         // 守卫表达式
}
```

- 匹配顺序从上到下，命中第一个即返回
- 绑定的变量只在 `->` 右侧有效，不泄漏到外部
- `{}` 匹配任何对象（部分匹配语义）
- `...` 必须出现在模式末尾
- 字面量匹配使用严格相等（`===`）
- 字面量前缀负号（`-5`、`-3.14`）作为字面量模式支持；其它一元表达式、`or` 模式、`as` 别名暂不支持
- 对象模式中同一字段名不得重复（解析期抛 `SyntaxException`）
- 空 `match { }` 即无任何 case 时抛 `SyntaxException`

## 编译期类型检查

Ruler 对字面量运算做静态检查，提前拦截明显非法的类型组合：

```ruler
"hello" - "world"     // SyntaxException: operator '-' requires numeric types
!42                    // SyntaxException: operator '!' requires BOOL
if "notbool" { }       // SyntaxException: condition must be BOOL
```

含未知类型变量的表达式（如 `1 + unknownVar`）不会报错，保留动态灵活性。

## CLI / REPL

```bash
# 交互式 Shell
java -jar ruler.jar

# 执行脚本文件
java -jar ruler.jar -f=script.ruler

# 限制循环次数和栈深度
java -jar ruler.jar -f=script.ruler -maxLoopNumber=100000 -maxStackDepth=500
```

## 扩展与集成

### 自定义导入加载器

实现 `CustomImportLoader` 接口（`match(path)` + `load(path)`），通过 `@ImportMatchOrder` 控制优先级。加载来源可以是文件系统、数据库、HTTP 等。

### SPI 扩展点

实现 `ConfigurationHook` 接口，在 `META-INF/services/com.kamijoucen.ruler.plugin.spi.ConfigurationHook` 注册。内置 `StdIoHook` 提供 `DeleteFile()`、`WriteNewText()`、`ReadAllText()` 等函数。

## 构建

```bash
mvn -q -DskipTests compile
mvn test
mvn -Dtest=BaseTest#arrayPushTest test
```

## 注意事项

- 脚本异常以 Java 异常形式抛出，引擎内部不提供 `try/catch`
- `RulerConfigurationImpl` 持有导入缓存，环境隔离需新建实例
- 编译期类型检查仅针对已知类型，动态场景保留运行时容错
- 语句末尾分号可选，换行即可作为语句结束；同一行多语句需用分号分隔
- 自定义中缀运算符不能跨行，遇到换行自动截断为独立语句
