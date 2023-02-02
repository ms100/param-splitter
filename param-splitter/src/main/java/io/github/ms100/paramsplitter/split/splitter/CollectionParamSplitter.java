package io.github.ms100.paramsplitter.split.splitter;

import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class CollectionParamSplitter implements SmartParamSplitter<Collection<?>> {

    @Override
    public List<Collection<?>> split(Collection<?> splitParamArg, int chunkSize) {
        int size = splitParamArg.size();
        List<Collection<?>> list = new ArrayList<>((size + chunkSize - 1) / chunkSize);

        Collection<Object> sub = new ArrayList<>(chunkSize);
        list.add(sub);
        for (Object item : splitParamArg) {
            if (sub.size() == chunkSize) {
                sub = new ArrayList<>(chunkSize);
                list.add(sub);
            }
            sub.add(item);
        }
        return list;
    }

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return Collection.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(ArrayList.class);
    }
}
