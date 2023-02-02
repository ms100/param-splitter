package io.github.ms100.paramsplitter.split;

import org.springframework.core.ResolvableType;

/**
 * @author zhumengshuai
 */
public interface SmartParamSplitter<T> extends ParamSplitter<T> {

    /**
     * 可以处理的类
     *
     * @param resolvableType 参数类型
     * @return ture为支持，false为不支持
     */
    boolean support(ResolvableType resolvableType);
}
