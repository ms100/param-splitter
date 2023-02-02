package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author Zhumengshuai
 */
public class LongResultMerger implements SmartResultMerger<Long> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        return Long.class.equals(clazz) || Long.TYPE.equals(clazz);
    }

    @Override
    public Long merge(List<Long> resultChunks) {
        return resultChunks.stream().mapToLong(Long.class::cast).sum();
    }
}
