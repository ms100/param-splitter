package io.github.ms100.paramsplitter.split;

/**
 * @author zhumengshuai
 */
public interface ParamSplitterRegistryAware {
    /**
     * 注入ParamSplitterService
     *
     * @param paramSplitterRegistry ParamSplitterService的bean
     */
    void setParamSplitterRegistry(ParamSplitterRegistry paramSplitterRegistry);
}
