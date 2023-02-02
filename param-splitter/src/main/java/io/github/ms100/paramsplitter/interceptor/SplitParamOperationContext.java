package io.github.ms100.paramsplitter.interceptor;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * 被{@link SplitParam @SplitParam}注解的方法，在拦截器执行时，方法上的上下文状态
 *
 * @author zhumengshuai
 */

@Getter
@Slf4j
class SplitParamOperationContext {

    private final SplitParamOperation operation;

    private final Object[] args;

    @Getter
    @Nullable
    private final Object splitParamArg;

    public SplitParamOperationContext(SplitParamOperation operation, Object[] args) {
        this.operation = operation;
        this.args = args;
        this.splitParamArg = args[operation.getSplitParamParameterPosition()];
    }

    public Method getMethod() {
        return operation.getMethod();
    }

    public Object[] getInvokeArg(Object subCacheAsMultiArg) {
        /**
         * args数组是{@link MethodInvocation#getArguments()}的引用
         * 改变了它就改变了{@link MethodInvocation#proceed()}的参数
         * 因为需要改变它，为了防止影响所以这里克隆下
         */
        Object[] invokeArgs = args.clone();
        invokeArgs[operation.getSplitParamParameterPosition()] = subCacheAsMultiArg;

        return invokeArgs;
    }
}