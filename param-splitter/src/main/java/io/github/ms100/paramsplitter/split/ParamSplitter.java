package io.github.ms100.paramsplitter.split;

import java.util.Collection;
import java.util.List;

/**
 * 参数分割器
 *
 * @author zhumengshuai
 */
public interface ParamSplitter<T> {

    /**
     * 是否需要分割
     *
     * @param splitParamArg 被分割的参数
     * @param chunkSize     每块大小
     * @return true为需要分割，false为不需要分割
     */
    default boolean needSplit(T splitParamArg, int chunkSize) {
        return ((Collection<?>) splitParamArg).size() > chunkSize;
    }

    /**
     * 分割参数
     *
     * @param splitParamArg 被分割的参数
     * @param chunkSize     每块大小
     * @return 分割后的列表
     */
    List<T> split(T splitParamArg, int chunkSize);

}
