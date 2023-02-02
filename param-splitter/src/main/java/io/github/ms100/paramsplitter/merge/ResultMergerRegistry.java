package io.github.ms100.paramsplitter.merge;

import io.github.ms100.paramsplitter.merge.merger.ArrayResultMerger;
import io.github.ms100.paramsplitter.merge.merger.BooleanResultMerger;
import io.github.ms100.paramsplitter.merge.merger.IntegerResultMerger;
import io.github.ms100.paramsplitter.merge.merger.ListResultMerger;
import io.github.ms100.paramsplitter.merge.merger.LongResultMerger;
import io.github.ms100.paramsplitter.merge.merger.MapResultMerger;
import io.github.ms100.paramsplitter.merge.merger.SetResultMerger;
import io.github.ms100.paramsplitter.merge.merger.VoidResultMerger;
import io.github.ms100.paramsplitter.merge.resolver.CompletableFutureResultMergerResolver;
import io.github.ms100.paramsplitter.merge.resolver.MonoResultMergerResolver;
import io.github.ms100.paramsplitter.merge.resolver.SmartResultMergerResolver;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class ResultMergerRegistry {
    private final List<ResultMergerResolver> mergerResolvers = new ArrayList<>();

    public ResultMergerRegistry(List<ResultMergerResolver> mergerResolvers, List<SmartResultMerger<?>> smartResultMergers) {
        addResolvers(mergerResolvers);
        addResolvers(getDefaultResultMergerResolvers(smartResultMergers));
    }

    private void addResolvers(List<ResultMergerResolver> mergerResolvers) {
        mergerResolvers.forEach(resolver -> {
            if (resolver instanceof ResultMergerRegistryAware) {
                ((ResultMergerRegistryAware) resolver).setResultMergerService(this);
            }
            this.mergerResolvers.add(resolver);
        });
    }

    private List<ResultMergerResolver> getDefaultResultMergerResolvers(List<SmartResultMerger<?>> smartResultMergers) {
        List<ResultMergerResolver> resolvers = new ArrayList<>();

        SmartResultMergerResolver smartResultMergerResolver = new SmartResultMergerResolver();
        smartResultMergerResolver.addMergers(smartResultMergers);
        smartResultMergerResolver.addMergers(getDefaultSmartResultMergers());
        resolvers.add(smartResultMergerResolver);

        resolvers.add(new MonoResultMergerResolver());
        resolvers.add(new CompletableFutureResultMergerResolver());

        return resolvers;
    }

    private List<SmartResultMerger<?>> getDefaultSmartResultMergers() {
        List<SmartResultMerger<?>> mergers = new ArrayList<>();

        mergers.add(new ArrayResultMerger<>());
        mergers.add(new ListResultMerger());
        mergers.add(new MapResultMerger());
        mergers.add(new SetResultMerger());
        mergers.add(new BooleanResultMerger());
        mergers.add(new IntegerResultMerger());
        mergers.add(new LongResultMerger());
        mergers.add(new VoidResultMerger());

        return mergers;
    }

    @Nullable
    public ResultMerger<Object> getMerger(ResolvableType resolvableType) {
        for (ResultMergerResolver mergerResolver : mergerResolvers) {
            ResultMerger<Object> merger = mergerResolver.resolve(resolvableType);
            if (merger != null) {
                return merger;
            }
        }

        return null;
    }
}
