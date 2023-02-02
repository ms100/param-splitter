package io.github.ms100.paramsplitter.merge;

import org.springframework.core.ResolvableType;

/**
 * @author zhumengshuai
 */
public interface SmartResultMerger<T> extends ResultMerger<T> {

    /**
     * 可以处理的类
     *
     * @param resolvableType 返回值类型
     * @return ture为支持，false为不支持
     */
    boolean support(ResolvableType resolvableType);
}
