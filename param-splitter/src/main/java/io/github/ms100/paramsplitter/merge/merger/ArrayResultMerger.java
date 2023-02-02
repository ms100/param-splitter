package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class ArrayResultMerger<T> implements SmartResultMerger<T> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        return resolvableType.isArray();
    }

    @Override
    public T merge(List<T> resultChunks) {
        int size = resultChunks.stream().mapToInt(Array::getLength).sum();
        T all = (T) Array.newInstance(resultChunks.get(0).getClass().getComponentType(), size);

        final int[] skip = {0};
        resultChunks.forEach(chunk -> {
            int chunkLength = Array.getLength(chunk);
            System.arraycopy(chunk, 0, all, skip[0], chunkLength);
            skip[0] += chunkLength;
        });

        return all;
    }
}
