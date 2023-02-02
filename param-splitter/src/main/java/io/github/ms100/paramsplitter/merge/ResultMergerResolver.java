package io.github.ms100.paramsplitter.merge;

import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * @author Zhumengshuai
 */
public interface ResultMergerResolver {

    /**
     * 如果可以处理，返回一个能处理参数ResolvableType的ResultMerger实例
     *
     * @param resolvableType 返回值类型
     * @return 对应的合并器，找不到支持的合并器返回null
     */
    @Nullable
    ResultMerger<Object> resolve(ResolvableType resolvableType);
}
