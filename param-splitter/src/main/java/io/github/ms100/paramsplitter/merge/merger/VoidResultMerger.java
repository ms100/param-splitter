package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author Zhumengshuai
 */
public class VoidResultMerger implements SmartResultMerger<Void> {

    @Override
    public boolean support(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        return Void.class.equals(clazz) || Void.TYPE.equals(clazz);
    }

    @Override
    public Void merge(List<Void> resultChunks) {
        return null;
    }
}
