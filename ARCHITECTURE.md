# 代码组织

Ruler 按“数据类型 + 处理逻辑”组织解释器。借鉴状态归属明确、逻辑无跨调用状态的原则，内部直接使用具体类型，不要求依赖倒置或依赖注入。

## 目录

- `types`：AST、Token、值、静态类型、模块描述、参数和结果，以及配置、扫描、解析、运行时状态。按用途划分子包。
- `logic`：词法分析、语法分析、编译、求值、类型检查、模块加载、值转换等算法，按功能划分子包。
- `stdlib`：内置函数和标准库初始化。
- `api`：Java 宿主使用的 `Ruler`、`RulerRunner` 入口。
- `cli`：命令行和交互式 Shell。

不再使用 `domain`、`component`、`application`、`service` 作为通用分层目录。核心代码不依赖 CLI 或 JLine。

## 状态归属

| 生命周期 | 状态拥有者 |
| --- | --- |
| 一个引擎实例 | `RulerConfiguration`：全局环境、执行限制、内置方法、`ModuleState` |
| 一个引擎的模块系统 | `ModuleState`：模块注册、编译缓存、宿主加载器 |
| 一次扫描 | `LexerState`：输入、偏移、行列、扫描缓冲 |
| 一次解析 | `ParseState`、`TokenStream`：游标和语法上下文 |
| 一次执行 | `RuntimeContext`：外部参数、控制流、调用深度、中缀函数和导入链 |
| 一个词法作用域 | `Scope`：变量绑定和父作用域；闭包保留定义环境 |

模块的子执行上下文共享当前导入链，独立启动的执行有独立导入链。循环计数属于一次循环，留在局部变量中。固定运算符表、转换器表和小整数缓存初始化后只读，不需要可替换的引擎组件。

`logic` 不把某次调用的可变数据保存到处理器实例或静态字段。允许局部变量、不可变策略参数和无状态处理器引用，也允许通过显式传入的状态对象推进解析或执行。无状态不等于纯函数。

## 数据与行为

AST 描述程序结构，通过 `accept` 完成 Visitor 分派。求值和类型检查入口分别位于 `EvalVisitor.evaluate`、`TypeCheckVisitor.check`，节点不选择运行时处理器、不保存运算实现。

数据类型可以保留构造校验、值构造、相等判断、集合查询更新，以及与自身不变量紧密相关的方法。避免为每个方法增加 Context 或包装对象。

初始化直接使用普通函数：`Builtins` 安装标准库，`ConfigurationHooks` 加载宿主 SPI。`RulerConfiguration` 提供可直接使用的默认环境，不再同时提供 Configuration/Impl 两套类型。

## 抽象的取舍

- `IntegerValue.valueOf` 管理整数值构造；小整数缓存是其私有实现。
- `Operations`、`ValueConversions` 维护固定只读查询表，不提供 Factory/Manager 替换接口。
- `ModuleLoader` 处理模块加载、编译与执行；缓存只是 `ModuleState` 的一部分。
- `CallLogic` 统一函数、闭包、绑定方法的调用和深度检查。
- 保留有实际用途的语法分派、值类型和运算协议，以及 `RulerFunction`、`CustomImportLoader`、`ConfigurationHook` 等宿主接口。

增加接口前，需要说明现有代码中真实的多态或宿主扩展需求。不要仅为跨包调用、单元测试或命名成对而增加接口、Factory、Manager、Impl。

## Java API 迁移

这是包名与内部 API 的破坏性迁移，不保留旧架构包的转发类。语言语法和常用 `Ruler.compile(...).run(...)` 使用方式保持一致。

| 原入口 | 新入口 |
| --- | --- |
| `service.Ruler`、`service.RulerRunner` | `api.Ruler`、`api.RulerRunner` |
| `application.impl.RulerConfigurationImpl` | `types.config.RulerConfiguration` |
| `domain.*` | 对应的 `types.*`；静态类型位于 `types.typing` |
| `logic.function.RulerFunction` | `types.spi.RulerFunction` |
| `component.option.CustomImportLoader` | `types.spi.CustomImportLoader` |
| `plugin.spi.ConfigurationHook` | `types.spi.ConfigurationHook` |
| `getConfigModuleManager().registerModule(...)` | `getModules().register(...)` |
| `getCustomImportLoadManager().registerCustomImportLoader(...)` | `getModules().registerLoader(...)` |

SPI 文件名同步改为 `META-INF/services/com.kamijoucen.ruler.types.spi.ConfigurationHook`。当前 runner 的 Java 序列化仍不支持，不承诺跨版本序列化兼容。

## 验证

运行 `mvn clean test`。HTTP 测试包含两个可选的外网请求；离线核心回归可运行 `mvn clean test '-Dtest=*,!HttpTest'`。架构测试保护包布局、核心入口依赖和逻辑状态归属，行为测试覆盖语法、求值、模块、值转换、状态隔离和异常恢复。
