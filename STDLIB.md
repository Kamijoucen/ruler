# Ruler 标准库

## 内置原生函数

以下函数无需 import，默认全局可用。

### 数学

```
abs(x)        min(a, b)     max(a, b)
round(x)      floor(x)      ceil(x)
pow(x, y)     sqrt(x)       random()
```

### 字符串

```
stringSubstring(str, start, end?)   stringIndexOf(str, sub, fromIndex?)
stringReplace(str, old, new)        stringSplit(str, regex)
stringTrim(str)                     stringUpperCase(str)
stringLowerCase(str)                stringStartsWith(str, prefix)
stringEndsWith(str, suffix)
```

### 数组（含高阶函数）

```
arrayMap(arr, fn)           arrayFilter(arr, fn)
arrayReduce(arr, fn, init?) arrayFind(arr, fn)
arrayFindIndex(arr, fn)     arraySlice(arr, start, end?)
arrayConcat(arr1, arr2)     arrayJoin(arr, sep)
arrayReverse(arr)           arrayPop(arr)
arrayShift(arr)             arrayUnshift(arr, val)
arrayIndexOf(arr, val)      arraySort(arr, comparator?)
```

### 对象

```
keys(obj)     values(obj)     hasKey(obj, key)     merge(obj1, obj2)
```

### 类型判断

```
isNull(x)      isNumber(x)    isString(x)    isBool(x)
isArray(x)     isFunction(x)  isDate(x)
```

### 其他旧函数

```
println(...)   ToNumber(x)    ToBoolean(x)   ToString(x)
Datetime(...)  Timestamp()    Proxy(...)     Call(name)
Panic(msg)
```

## 对象方法

可直接对字面量调用的方法：

- **字符串**：`length()` `substring()` `indexOf()` `replace()` `split()` `trim()` `upperCase()` `lowerCase()` `startsWith()` `endsWith()` `charAt()`
- **数组**：`length()` `push()` `pop()` `shift()` `unshift()` `slice()` `reverse()` `concat()` `join()` `indexOf()` `sort()` `map()` `filter()` `reduce()` `find()` `findIndex()`

## 脚本模块

位于 `resources/ruler/std/` 下，按需 import：

```
import "/ruler/std/collections.txt";  // IsArray / IsEmpty / Contains / Swap
import "/ruler/std/util.txt";         // Eq / EqArrayEveryOne / EqArrayAnyOne...
import "/ruler/std/sort.txt" sort;   // Sort(arr, compare)
import "/ruler/std/string.txt";       // 字符串函数的脚本别名包装
```

## 命名约定

- **新函数**统一使用小写驼峰（`abs` / `arrayMap` / `isNull`）。
- **旧保留函数**维持原帕斯卡命名（`ToNumber` / `Add` / `Datetime`）。
