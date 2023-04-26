# ParamSplitter

[中文](./README.md)

## Installation

### Maven

```xml

<dependency>
	<groupId>io.github.ms100</groupId>
	<artifactId>param-splitter</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Usage

When there is a size limitation on a parameter of a method, which is a collection parameter, add the `@SplitParam`
annotation to this parameter. When calling the method, if the size of the collection parameter exceeds the limit, the
parameter will be split and executed in a loop, and the results will be merged and returned in the end. When calling
this method, you don't need to worry about the size limit of the collection parameter.

> At most one parameter in a method can be annotated with `@SplitParam`.

### Enable Functionality

Enable the functionality by adding the `@EnableParamSplitter` annotation to the entry class or configuration class. For
example:

```java

@EnableParamSplitter
@Configuration
public class xxConfig {
	//...
}
```

`@EnableParamSplitter.order()` is used to configure the order of applying multiple enhancements at the same join point,
with the default being the lowest priority `Ordered.LOWEST_PRECEDENCE`. When used with other AOP annotations, be sure to
adjust it accordingly. For example:

```java

@EnableCaching(order = Ordered.LOWEST_PRECEDENCE - 1) // Cache first, so increase priority
@EnableParamSplitter
@Configuration
public class xxConfig {
	//...
}
```

### Annotation on Method Parameters

#### Splitting Collections

Add annotations to the parameters of the method and configure the quantity limit.

```java
public class FoxService {

	public Map<Integer, Foo> getMultiFoo(@SplitParam(3) Collection<Integer> fooIds) {
		// The size of fooIds is limited to 3
		//...
	}

	public List<Foo> getMultiFoo(@SplitParam(5) List<Integer> fooIds) {
		// The size of fooIds is limited to 5
		//...
	}
}
```

#### Object Splitting

If all the parameters are encapsulated in one class, add this annotation to the attributes that need to be split (
splittable).

```java
public class FoxService {

	public List<Foo> getMultiFoo(@SplitParam(5) SearchParam param) {
		// SearchParam.nums is limited to 5
		//...
	}
}

public class SearchParam {

	@SplitParam(3) // If chunkSize is not set in the method parameter, it will be 3
	private final Collection<Integer> nums;
}
```

> The class used as a parameter needs to implement the `Cloneable` interface or have a parameterless constructor.
> The type of splittable attribute can also be a splittable class, but be careful not to cause an infinite loop.

## Support and Extension

### Parameter Splitting

The following method parameter types that support splitting are supported by default:

* Various types of arrays
* `java.util.List` and classes that implement `java.util.List`
* `java.util.Collection`
* `java.util.Set` and `java.util.HashSet`
* Classes with the `@SplitParam` annotation on their attributes

If you need to split more types, you can extend it by implementing `SmartParamSplitter`
or `ParamSplitter`, `ParamSplitterResolver`.

### Result Merging

The following return value types that support merging are supported by default:

* Various types of arrays
* `java.util.Collection`
* `java.util.List` and `java.util.ArrayList`
* `java.util.Set` and `java.util.HashSet`
* `java.util.Map` and `java.util.HashMap`
* `java.lang.Void`
* `java.lang.Boolean` (merged with logic)
* `java.lang.Integer` (sum)
* `java.lang.Long` (sum)
* `CompletableFuture` and `Mono` with supported types as generics.

If you need to merge more types, you can implement `SmartResultMerger` or `ResultMergerResolver`, `ResultMerger` on your
own.

## How It Works

### AOP

1. Introduce the `ParamSplitterAutoConfiguration` configuration class by using the `@EnableParamSplitter` annotation.
2. In the `ParamSplitterAutoConfiguration` configuration class, define Beans
	* `SplitParamOperationSource` (used to generate Pointcut) and `SplitParamInterceptor` (Advice);
	* Define `SplitParamOperationSourceAdvisor` (Advisor) and inject the first two Beans to generate Pointcut.
3. Spring gets the Pointcut of the Advisor, loops through all the methods as parameters, and calls the matches method of
   the Pointcut. When a matching method is found, a dynamic proxy is created on the method, and the Advice is called
   when the proxy method is executed (the description is not rigorous, but this is roughly the meaning).

## Development Summary

### Small knowledge points

- `ProxyMethodInvocation` is a call chain, and each Advice on the chain will only be executed once. If multiple
  executions are required, use `invocableClone()` or `invocableClone(Object... arguments)`.

