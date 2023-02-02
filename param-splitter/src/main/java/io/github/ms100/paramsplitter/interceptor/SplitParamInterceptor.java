package io.github.ms100.paramsplitter.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AOP方法拦截器
 *
 * @author zhumengshuai
 */
@RequiredArgsConstructor
@Slf4j
public class SplitParamInterceptor implements MethodInterceptor {

    private final SplitParamOperationSource operationSource;

    @Override
    @Nullable
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Assert.state(invocation instanceof ProxyMethodInvocation,
                "Invocation must be ProxyMethodInvocation");
        Method method = invocation.getMethod();
        Object target = invocation.getThis();
        Assert.state(target != null, "Target must not be null");

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        SplitParamOperation operation = operationSource.getOperation(method, targetClass);

        if (operation != null) {
            SplitParamOperationContext context = new SplitParamOperationContext(operation, invocation.getArguments());

            return execute(context, (ProxyMethodInvocation) invocation);
        }

        return invocation.proceed();
    }

    @SneakyThrows
    @Nullable
    public Object execute(SplitParamOperationContext context, ProxyMethodInvocation invocation) {

        Object splitParamArg = context.getSplitParamArg();
        SplitParamOperation operation = context.getOperation();
        // 如果@SplitParam注解的参数值为null、空集合或者数量没有超过限制，则调用原方法返回
        if (splitParamArg == null || !operation.needSplit(splitParamArg)) {
            return invocation.proceed();
        }
        List<?> argChunks = operation.splitArg(splitParamArg);
        List<Object> resultChunks = new ArrayList<>();
        for (Object argChunk : argChunks) {
            Object o = invokeOperation(context, invocation, argChunk);
            resultChunks.add(o);
        }

        return operation.mergeResult(resultChunks);
    }

    @SneakyThrows
    @Nullable
    public Object invokeOperation(SplitParamOperationContext context,
                                  ProxyMethodInvocation invocation, Object splitParamArgChunk) {

        Object[] invokeArg = context.getInvokeArg(splitParamArgChunk);

        return invocation.invocableClone(invokeArg).proceed();
    }
}
