# ParamSplitter

## 安装
### Maven
```xml

<dependency>
  <groupId>io.github.ms100</groupId>
  <artifactId>param-splitter</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 使用

当方法对其参数中的某个【集合参数】的大小有限制时，在此参数上加上 `@SplitParam` 注解。在调用方法时，当【集合参数】的大小超过限制会将【集合参数】分块后循环执行，最后将结果合并返回。调用此方法时就不必关心【集合参数】的大小限制了。

> 一个方法中最多有一个参数被 `@SplitParam` 注解。

### 开启功能
通过在入口类或配置类上增加 `@EnableParamSplitter` 注解开启功能。例如：
```java
@EnableParamSplitter
@Configuration
public class xxConfig {
    //...
}
```
`@EnableParamSplitter.order()` 用来配置在同一连接点应用多个增强时的顺序，默认为最低优先级 `Ordered.LOWEST_PRECEDENCE`。在与其他 AOP 注解搭配时需注意调整。例如：

```java
@EnableCaching(order = Ordered.LOWEST_PRECEDENCE - 1) //先取缓存所以提高优先级
@EnableParamSplitter
@Configuration
public class xxConfig {
    //...
}
```

### 注解方法参数

#### 拆分集合
在方法的参数上加上注解，并配置数量限制。
```java
public class FoxService {

    public Map<Integer, Foo> getMultiFoo(@SplitParam(3) Collection<Integer> fooIds) {
      // fooIds大小限制为3个
      //...
    }

    public List<Foo> getMultiFoo(@SplitParam(5) List<Integer> fooIds) {
      // fooIds大小限制为5个
      //...
    }
}
```

#### 拆分对象
如果用一个类封装了所有参数，则在需要拆分的属性(可拆分)上增加本注解。
```java
public class FoxService {

    public List<Foo> getMultiFoo(@SplitParam(5) SearchParam param) {
      // SearchParam.nums大小限制为5个
      //...
    }
}

public class SearchParam {

    @SplitParam(3)//若方法参数中没有设置chunkSize，则为3
    private final Collection<Integer> nums;
}
```
> 作为参数的类需要实现 `Cloneable` 接口，或拥有无参构造方法。
> 可拆分属性的类型还可以是一个可拆分的类，但注意不要引起死循环。

## 支持和扩展
### 参数分割
默认支持分割的方法参数类型如下：
* 各种类型的数组
* `java.util.List`、实现了 `java.util.List` 的类
* `java.util.Collection`
* `java.util.Set`、`java.util.HashSet`
* 属性带`@SplitParam`注解的类

如果需要对更多类型进行分割可通过实现 `SmartParamSplitter` 或 `ParamSplitter`、`ParamSplitterResolver` 进行扩展。

### 结果合并
默认支持合并的返回值类型如下：
* 各种类型的数组
* `java.util.Collection`
* `java.util.List`、`java.util.ArrayList`
* `java.util.Set`、`java.util.HashSet`
* `java.util.Map`、`java.util.HashMap`
* `java.lang.Void`
* `java.lang.Boolean`（与逻辑合并）
* `java.lang.Integer`（求和）
* `java.lang.Long`（求和）
* 用支持的类型作为泛型的 `CompletableFuture`、`Mono`。

如果需要对更多类型进行合并可以自行实现 `SmartResultMerger` 或 `ResultMergerResolver`、`ResultMerger`。

## 工作原理
### AOP
1. 通过  `@EnableParamSplitter` 注解引入 `ParamSplitterAutoConfiguration` 配置类。
2. `ParamSplitterAutoConfiguration` 配置类中定义 Bean
	* `SplitParamOperationSource`(用来生成 Pointcut)、`SplitParamInterceptor`(Advice)；
	* 定义 `SplitParamOperationSourceAdvisor`(Advisor) 并注入前两个 Bean，生成 Pointcut。
3. Spring 获取 Advisor 的 Pointcut，用所有方法做参数，循环调用 Pointcut 的 matches 方法，在匹配的方法上建立动态代理，当代理方法执行时调用 Advice（描述不严谨，大概这么个意思）。

## 开发总结

### 小知识点
- ProxyMethodInvocation 是一个调用链，链上的每一个 Advice 只会执行一次。如果需要多次执行，需要用 `invocableClone()` 或 `invocableClone(Object... arguments)`。

