package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Zhumengshuai
 */
public class SetResultMerger implements SmartResultMerger<Set<?>> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return Collection.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(HashSet.class);
    }

    @Override
    public Set<?> merge(List<Set<?>> resultChunks) {
        Integer size = resultChunks.stream().map(Set::size).reduce(0, Integer::sum);
        Set<Object> set = new HashSet<>((int) (size / 0.75f));
        resultChunks.forEach(set::addAll);
        return set;
    }
}