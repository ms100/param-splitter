package io.github.ms100.paramsplitter.merge.resolver;

import io.github.ms100.paramsplitter.annotation.EnableParamSplitter;
import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableParamSplitter
class MonoResultMergerResolverTest {
    @Autowired
    private MonoResultMergerResolver resolver;
    @Autowired
    private ResultMergerRegistry service;

    @Test
    void resolve() {
        resolver.setResultMergerService(service);
        ResultMerger<Object> merger = resolver.resolve(ResolvableType.forMethodReturnType(ReflectionUtils.findMethod(getClass(), "test")));
        assertNotNull(merger);
        System.out.println(merger);
        Mono<Void> v = Mono.empty();
        List<Object> monos = Arrays.asList(v, v, v);
        Mono<?> mono = (Mono<?>) merger.merge(monos);
        System.out.println(mono.block());
    }

    public Mono<Void> test() {
        return null;
    }
}