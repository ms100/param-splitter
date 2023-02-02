package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author Zhumengshuai
 */
public class IntegerResultMerger implements SmartResultMerger<Integer> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        return Integer.class.equals(clazz) || Integer.TYPE.equals(clazz);
    }

    @Override
    public Integer merge(List<Integer> resultChunks) {
        return resultChunks.stream().mapToInt(Integer.class::cast).sum();
    }
}
