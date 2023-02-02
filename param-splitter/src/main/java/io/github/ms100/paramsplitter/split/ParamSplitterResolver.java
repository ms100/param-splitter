package io.github.ms100.paramsplitter.split;

import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * @author zhumengshuai
 */
public interface ParamSplitterResolver {

    /**
     * 如果可以处理，返回一个能处理参数ResolvableType的ParamSplitter实例
     *
     * @param resolvableType 参数类型
     * @return 对应的分割器，找不到支持的分割器返回null
     */
    @Nullable
    ParamSplitter<Object> resolve(ResolvableType resolvableType);
}
