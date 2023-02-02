package io.github.ms100.paramsplitter.split.splitter;

import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class ListParamSplitter implements SmartParamSplitter<List<?>> {

    @Override
    public List<List<?>> split(List<?> splitParamArg, int chunkSize) {
        int size = splitParamArg.size();
        List<List<?>> list = new ArrayList<>((size + chunkSize - 1) / chunkSize);

        int skip = 0;
        while (skip < size) {
            List<?> sub = splitParamArg.subList(skip, Math.min(skip += chunkSize, size));
            list.add(sub);
        }
        return list;
    }

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return List.class.isAssignableFrom(clazz);
    }
}
