package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author Zhumengshuai
 */
public class BooleanResultMerger implements SmartResultMerger<Boolean> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        return Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz);
    }

    @Override
    public Boolean merge(List<Boolean> resultChunks) {
        final boolean[] bool = {true};
        resultChunks.forEach(chunk -> bool[0] = bool[0] && chunk);
        return bool[0];
    }
}