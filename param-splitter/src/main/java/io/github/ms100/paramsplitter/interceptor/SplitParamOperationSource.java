package io.github.ms100.paramsplitter.interceptor;

import io.github.ms100.paramsplitter.annotation.SplitParamAnnotationUtils;
import io.github.ms100.paramsplitter.annotation.SplitParamParameterDetail;
import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistry;
import io.github.ms100.paramsplitter.split.ParamSplitter;
import io.github.ms100.paramsplitter.split.ParamSplitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhumengshuai
 */
@Slf4j
@RequiredArgsConstructor
public class SplitParamOperationSource {

    private static final Object NULL_CACHING_ATTRIBUTE = new Object();

    private final Map<Object, Object> cache = new ConcurrentHashMap<>(1024);

    private final ParamSplitterRegistry paramSplitterRegistry;
    private final ResultMergerRegistry resultMergerRegistry;

    @Nullable
    public SplitParamOperation getOperation(Method method, @Nullable Class<?> targetClass) {
        Object cacheKey = getCacheKey(method, targetClass);
        Object cached = cache.get(cacheKey);

        if (cached != null) {
            return cached != NULL_CACHING_ATTRIBUTE ? (SplitParamOperation) cached : null;
        } else {
            SplitParamOperation operation = computeOperation(method, targetClass);
            if (operation != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Adding splitParam method '" + method.getName() + "' with operation: " + operation);
                }
                cache.put(cacheKey, operation);
            } else {
                cache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
            }
            return operation;
        }
    }

    @Nullable
    private SplitParamOperation computeOperation(Method method, @Nullable Class<?> targetClass) {

        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

        SplitParamOperation operation = findOperation(specificMethod);
        if (operation != null) {
            return operation;
        }

        if (specificMethod != method) {
            return findOperation(method);
        }
        return null;
    }


    private Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    @Nullable
    protected SplitParamOperation findOperation(Method method) {
        SplitParamParameterDetail parameterDetail = SplitParamAnnotationUtils.findAnnotation(method);

        if (parameterDetail == null) {
            return null;
        }

        return createOperation(method, parameterDetail);
    }

    protected SplitParamOperation createOperation(Method method, SplitParamParameterDetail parameterDetail) {
        ResolvableType paramType = ResolvableType.forMethodParameter(method, parameterDetail.getPosition());
        ParamSplitter<Object> splitter = paramSplitterRegistry.getSplitter(paramType);
        if (splitter == null) {
            throw new IllegalStateException("The @SplitParam parameter type " + paramType.resolve() + " is not supported on " + method);
        }

        ResolvableType returnType = ResolvableType.forMethodReturnType(method);
        ResultMerger<Object> merger = resultMergerRegistry.getMerger(returnType);
        if (merger == null) {
            throw new IllegalStateException("the return type " + returnType.resolve() + " is not supported on " + method);
        }

        return new SplitParamOperation(method, parameterDetail, splitter, merger);
    }
}
