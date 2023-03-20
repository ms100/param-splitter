package io.github.ms100.paramsplitter.merge.resolver;

import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistry;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistryAware;
import io.github.ms100.paramsplitter.merge.ResultMergerResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author Zhumengshuai
 */
public class CompletableFutureResultMergerResolver implements ResultMergerResolver, ResultMergerRegistryAware {

    @Nullable
    private ResultMergerRegistry resultMergerRegistry;

    private final ConcurrentMap<ResultMerger<Object>, CompletableFutureMerger> cache = new ConcurrentHashMap<>();

    @Override
    public void setResultMergerService(ResultMergerRegistry resultMergerRegistry) {
        this.resultMergerRegistry = resultMergerRegistry;
    }

    @Override
    public ResultMerger<Object> resolve(ResolvableType resolvableType) {
        Class<?> clazz = resolvableType.resolve();
        if (!CompletableFuture.class.equals(clazz)) {
            return null;
        }

        ResolvableType generic = resolvableType.getGeneric();
        assert resultMergerRegistry != null;
        ResultMerger<Object> genericMerger = resultMergerRegistry.getMerger(generic);
        if (genericMerger == null) {
            return null;
        }

        ResultMerger<?> completableFutureMerger = cache.computeIfAbsent(genericMerger, CompletableFutureMerger::new);
        return (ResultMerger<Object>) completableFutureMerger;
    }

    @RequiredArgsConstructor
    static class CompletableFutureMerger implements ResultMerger<CompletableFuture<?>> {

        private final ResultMerger<Object> genericMerger;

        @Override
        public CompletableFuture<?> merge(List<CompletableFuture<?>> resultChunks) {
            CompletableFuture<?>[] completableFutures = resultChunks.toArray(new CompletableFuture<?>[0]);
            CompletableFuture<Void> all = CompletableFuture.allOf(completableFutures);
            return all.thenApply(c -> {
                List<Object> collect = resultChunks.stream().map(CompletableFuture::join).collect(Collectors.toList());
                return genericMerger.merge(collect);
            });
        }
    }
}