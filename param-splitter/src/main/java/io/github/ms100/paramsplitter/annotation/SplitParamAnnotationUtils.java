package io.github.ms100.paramsplitter.annotation;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author zhumengshuai
 */
public abstract class SplitParamAnnotationUtils {
    @Nullable
    public static SplitParamParameterDetail findAnnotation(Method method) {
        SplitParamParameterDetail detail = null;
        int parameterCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterCount; i++) {
            Parameter parameter = parameters[i];
            SplitParam annotation = parameter.getAnnotation(SplitParam.class);
            if (annotation != null) {
                if (detail == null) {
                    if (annotation.chunkSize() < 1) {
                        throw new IllegalStateException("The @SplitParam chunkSize should be greater than 0 on " + method);
                    }
                    detail = new SplitParamParameterDetail(method, i);
                } else {
                    throw new IllegalStateException("There can be only one @SplitParam annotation in method parameters on " + method);
                }
            }
        }

        return detail;
    }
}
