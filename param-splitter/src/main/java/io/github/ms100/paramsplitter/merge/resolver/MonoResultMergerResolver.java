package io.github.ms100.paramsplitter.merge.resolver;

import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.merge.ResultMergerResolver;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistry;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistryAware;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author Zhumengshuai
 */
public class MonoResultMergerResolver implements ResultMergerResolver, ResultMergerRegistryAware {

    @Nullable
    private ResultMergerRegistry resultMergerRegistry;

    private final ConcurrentMap<ResultMerger<Object>, MonoMerger> cache = new ConcurrentHashMap<>();

    @Override
    public void setResultMergerService(ResultMergerRegistry resultMergerRegistry) {
        this.resultMergerRegistry = resultMergerRegistry;
    }

    @Override
    public ResultMerger<Object> resolve(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        if (!Mono.class.equals(clazz)) {
            return null;
        }

        ResolvableType generic = resolvableType.getGeneric();
        assert resultMergerRegistry != null;
        ResultMerger<Object> genericMerger = resultMergerRegistry.getMerger(generic);
        if (genericMerger == null) {
            return null;
        }

        ResultMerger<?> monoMerger = cache.computeIfAbsent(genericMerger, MonoMerger::new);
        return (ResultMerger<Object>) monoMerger;
    }

    @RequiredArgsConstructor
    static class MonoMerger implements ResultMerger<Mono<?>> {

        private final ResultMerger<Object> genericMerger;

        @Override
        public Mono<?> merge(List<Mono<?>> resultChunks) {
            Flux<Object> flux = Flux.mergeSequential(resultChunks);
            Mono<List<Object>> mono = flux.collect(Collectors.toList());
            return mono.mapNotNull(genericMerger::merge);
        }
    }
}