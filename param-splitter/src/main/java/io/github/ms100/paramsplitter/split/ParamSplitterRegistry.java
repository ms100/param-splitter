package io.github.ms100.paramsplitter.split;

import io.github.ms100.paramsplitter.split.resolver.AnnotatedParamSplitterResolver;
import io.github.ms100.paramsplitter.split.resolver.SmartParamSplitterResolver;
import io.github.ms100.paramsplitter.split.splitter.ArrayParamSplitter;
import io.github.ms100.paramsplitter.split.splitter.CollectionParamSplitter;
import io.github.ms100.paramsplitter.split.splitter.ListParamSplitter;
import io.github.ms100.paramsplitter.split.splitter.SetParamSplitter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class ParamSplitterRegistry {
    private final List<ParamSplitterResolver> splitterResolvers = new ArrayList<>();

    public ParamSplitterRegistry(List<ParamSplitterResolver> splitterResolvers, List<SmartParamSplitter<?>> smartParamSplitters) {
        addResolvers(splitterResolvers);
        addResolvers(getDefaultParamSplitterResolvers(smartParamSplitters));
    }

    private void addResolvers(List<ParamSplitterResolver> splitterResolvers) {
        splitterResolvers.forEach(resolver -> {
            if (resolver instanceof ParamSplitterRegistryAware) {
                ((ParamSplitterRegistryAware) resolver).setParamSplitterRegistry(this);
            }

            this.splitterResolvers.add(resolver);
        });
    }

    private List<ParamSplitterResolver> getDefaultParamSplitterResolvers(List<SmartParamSplitter<?>> smartParamSplitters) {
        List<ParamSplitterResolver> resolvers = new ArrayList<>();

        SmartParamSplitterResolver smartParamSplitterResolver = new SmartParamSplitterResolver();
        smartParamSplitterResolver.addSplitters(smartParamSplitters);
        smartParamSplitterResolver.addSplitters(getDefaultSmartParamSplitters());
        resolvers.add(smartParamSplitterResolver);

        resolvers.add(new AnnotatedParamSplitterResolver());

        return resolvers;
    }

    private List<SmartParamSplitter<?>> getDefaultSmartParamSplitters() {
        List<SmartParamSplitter<?>> splitters = new ArrayList<>();

        splitters.add(new ArrayParamSplitter<>());
        splitters.add(new ListParamSplitter());
        splitters.add(new CollectionParamSplitter());
        splitters.add(new SetParamSplitter());

        return splitters;
    }

    @Nullable
    public ParamSplitter<Object> getSplitter(ResolvableType resolvableType) {
        for (ParamSplitterResolver splitterResolver : splitterResolvers) {
            ParamSplitter<Object> splitter = splitterResolver.resolve(resolvableType);
            if (splitter != null) {
                return splitter;
            }
        }

        return null;
    }
}
