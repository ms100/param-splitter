package io.github.ms100.paramsplitter.merge;

import java.util.List;

/**
 * 结果合并器
 *
 * @author Zhumengshuai
 */
public interface ResultMerger<T> {

    /**
     * 处理返回结果
     *
     * @param resultChunks 分批执行的结果
     * @return 合并后的结果
     */
    T merge(List<T> resultChunks);
}



