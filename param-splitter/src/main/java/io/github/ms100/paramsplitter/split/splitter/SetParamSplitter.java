package io.github.ms100.paramsplitter.split.splitter;

import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhumengshuai
 */
public class SetParamSplitter implements SmartParamSplitter<Set<?>> {

    @Override
    public List<Set<?>> split(Set<?> splitParamArg, int chunkSize) {
        int size = splitParamArg.size();
        List<Set<?>> list = new ArrayList<>((size + chunkSize - 1) / chunkSize);

        Set<Object> sub = new HashSet<>((int) (chunkSize / 0.75f));
        list.add(sub);
        for (Object item : splitParamArg) {
            if (sub.size() == chunkSize) {
                sub = new HashSet<>((int) (chunkSize / 0.75f));
                list.add(sub);
            }
            sub.add(item);
        }
        return list;
    }

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return Collection.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(HashSet.class);
    }
}
