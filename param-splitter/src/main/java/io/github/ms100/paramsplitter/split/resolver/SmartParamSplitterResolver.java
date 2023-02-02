package io.github.ms100.paramsplitter.split.resolver;

import io.github.ms100.paramsplitter.split.ParamSplitter;
import io.github.ms100.paramsplitter.split.ParamSplitterResolver;
import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class SmartParamSplitterResolver implements ParamSplitterResolver {

    private final List<SmartParamSplitter<?>> paramSplitters = new ArrayList<>();

    public void addSplitters(List<SmartParamSplitter<?>> paramSplitters) {
        this.paramSplitters.addAll(paramSplitters);
    }

    @Override
    public ParamSplitter<Object> resolve(ResolvableType resolvableType) {
        for (SmartParamSplitter<?> paramSplitter : paramSplitters) {
            if (paramSplitter.support(resolvableType)) {
                return (ParamSplitter<Object>) paramSplitter;
            }
        }

        return null;
    }
}
