package io.github.ms100.paramsplitter.merge;

/**
 * @author zhumengshuai
 */
public interface ResultMergerRegistryAware {
    /**
     * 注入ResultMergerService
     *
     * @param resultMergerRegistry ResultMergerService的bean
     */
    void setResultMergerService(ResultMergerRegistry resultMergerRegistry);
}
