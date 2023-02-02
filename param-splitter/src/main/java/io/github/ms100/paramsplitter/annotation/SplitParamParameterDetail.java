package io.github.ms100.paramsplitter.annotation;

import lombok.Getter;
import lombok.ToString;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * {@link SplitParam @SplitParam}注解的参数的详细信息
 *
 * @author Zhumengshuai
 */
@ToString
@Getter
public class SplitParamParameterDetail {

    /**
     * {@link SplitParam @SplitParam}注解的参数位置
     */
    private final int position;

    /**
     * {@link SplitParam @SplitParam}注解
     */
    private final SplitParam annotation;

    public SplitParamParameterDetail(Method method, int position) {
        Parameter parameter = method.getParameters()[position];
        this.position = position;
        // 用spring的工具获取注解使AliasFor生效
        this.annotation = Objects.requireNonNull(AnnotationUtils.getAnnotation(parameter, SplitParam.class));
    }

}