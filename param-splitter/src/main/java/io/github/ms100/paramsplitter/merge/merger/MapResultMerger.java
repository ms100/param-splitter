package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhumengshuai
 */
public class MapResultMerger implements SmartResultMerger<Map<?, ?>> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.toClass();
        return Map.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(HashMap.class);
    }

    @Override
    public Map<?, ?> merge(List<Map<?, ?>> resultChunks) {
        Integer size = resultChunks.stream().map(Map::size).reduce(0, Integer::sum);
        Map<Object, Object> map = CollectionUtils.newHashMap(size);
        resultChunks.forEach(map::putAll);
        return map;
    }

}
