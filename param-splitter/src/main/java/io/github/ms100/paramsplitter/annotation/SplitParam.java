package io.github.ms100.paramsplitter.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若方法对入参中的集合参数大小有要求，可以在参数上加上本注解，将会把超过限制的参数分块循环执行，并将返回结果合并。此时调用方就无需关注参数集合的大小限制了。
 *
 * <p>一个方法中最多有一个参数被 {@link SplitParam @SplitParam} 注解。</p>
 *
 * <p><b>使用说明：</b></p>
 * <p>
 * 通过在配置类上增加 {@link EnableParamSplitter @EnableParamSplitter} 注解开启功能。例如：
 * <pre><code>
 * &#64;EnableParamSplitter
 * &#64;Configuration
 * public class xxConfig {
 *     ...
 * }
 * </code></pre>
 * <p>
 * {@link EnableParamSplitter#order()} 用来配置在同一连接点应用多个通知时的顺序，默认为最低优先级 {@link Ordered#LOWEST_PRECEDENCE}。在与其他 AOP 注解搭配时需注意调整。例如：
 *
 * <pre><code>
 * &#64;EnableCaching(order = Ordered.LOWEST_PRECEDENCE - 1) //先取缓存所以提高优先级
 * &#64;EnableParamSplitter
 * &#64;Configuration
 * public class xxConfig {
 *     ...
 * }
 * </code></pre>
 * <p>
 * 在方法的参数上加上注解，并配置数量限制。
 * <pre><code>
 * public class FoxService {
 *
 *     public Map&lt;Integer, Foo&gt; getMultiFoo(&#64;SplitParam(10) Collection&lt;Integer&gt; fooIds) {
 *       // fooIds大小限制为10个
 *       ...
 *     }
 * }
 * </code></pre>
 * <p>
 * 如果用一个类封装了所有参数，则在需要拆分的属性(可拆分)上增加本注解。
 * <pre><code>
 * public class FoxService {
 *
 *     public List&lt;Foo&gt; getMultiFoo(&#64;SplitParam(5) SearchParam param) {
 *       // SearchParam.nums大小限制为5个
 *       ...
 *     }
 * }
 *
 * public class SearchParam {
 *
 *     &#64;SplitParam(3)//若方法参数中没有设置chunkSize，则为3
 *     private final Collection&lt;Integer&gt; nums;
 * }
 * </code></pre>
 * <ul>
 * <li>作为参数的类需要实现 {@link Cloneable} 接口，或拥有无参构造方法。</li>
 * <li>可拆分属性的类型还可以是一个可拆分的类，但注意不要引起死循环。</li>
 * </ul>
 * <p>
 * <b>总结和补充：</b>
 * <ol>
 * <li>可分割的参数类型包括 Collection、List、Set、数组、属性带注解的类。可通过实现 ParamSplitter、SmartParamSplitter、ParamSplitterResolver 进行扩展。</li>
 * <li>可合并的返回值类型包括 Map、Collection、List、Set、void、boolean（与逻辑合并）、int（求和）、long（求和），以及上述类型作为泛型的 CompletableFuture、Mono。可通过实现 ResultMerger、SmartResultMerger、ResultMergerResolver 进行扩展</li>
 * </ol>
 *
 * @author zhumengshuai
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitParam {
    /**
     * 按照chunkSize大小分割参数，方法参数上的注解的优先级高于字段上的。
     *
     * @return 分割大小
     */
    @AliasFor("chunkSize")
    int value() default Integer.MAX_VALUE;

    /**
     * 按照chunkSize大小分割参数，方法参数上的注解的优先级高于字段上的。
     *
     * @return 分割大小
     */
    @AliasFor("value")
    int chunkSize() default Integer.MAX_VALUE;
}
