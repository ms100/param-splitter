package io.github.ms100.paramsplitter.annotation;

import io.github.ms100.paramsplitter.config.ParamSplitterConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启参数分割功能，详细说明见{@link SplitParam @SplitParam}。
 *
 * @author zhumengshuai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ParamSplitterConfiguration.class)
public @interface EnableParamSplitter {

    /**
     * 在同一连接点应用多个增强时的执行的优先级。
     *
     * @return 执行优先级
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
