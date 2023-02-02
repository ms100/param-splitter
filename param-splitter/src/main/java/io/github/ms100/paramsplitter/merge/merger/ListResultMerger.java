package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Zhumengshuai
 */
public class ListResultMerger implements SmartResultMerger<List<?>> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return Collection.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(ArrayList.class);
    }

    @Override
    public List<?> merge(List<List<?>> resultChunks) {
        Integer size = resultChunks.stream().map(List::size).reduce(0, Integer::sum);
        List<Object> list = new ArrayList<>(size);
        resultChunks.forEach(list::addAll);
        return list;
    }
}