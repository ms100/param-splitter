package io.github.ms100.paramsplitter.split.splitter;

import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class ArrayParamSplitter<T> implements SmartParamSplitter<T> {

    @Override
    public boolean needSplit(T splitParamArg, int chunkSize) {
        return Array.getLength(splitParamArg) > chunkSize;
    }

    @Override
    public List<T> split(T splitParamArg, int chunkSize) {
        int size = Array.getLength(splitParamArg);
        List<T> list = new ArrayList<>((size + chunkSize - 1) / chunkSize);

        int skip = 0;
        while (skip < size) {
            T sub = (T) Array.newInstance(splitParamArg.getClass().getComponentType(), Math.min(chunkSize, size - skip));
            System.arraycopy(splitParamArg, skip, sub, 0, Array.getLength(sub));
            list.add(sub);
            skip += chunkSize;
        }
        return list;
    }

    @Override
    public boolean support(ResolvableType resolvableType) {
        return resolvableType.isArray();
    }
}
