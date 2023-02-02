package io.github.ms100.paramsplitter.merge.merger;

import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.assertj.core.util.Lists;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zhumengshuai
 */
class BooleanResultMergerTest {

    private SmartResultMerger<Boolean> merger = new BooleanResultMerger();

    @ParameterizedTest
    @MethodSource
    void merge(boolean expected, List<Boolean> list) {
        assertEquals(expected, merger.merge(list));
    }

    static List<Arguments> merge() {
        return Lists.list( // arguments:
                Arguments.of(false, Lists.list(true, true, false)),
                Arguments.of(false, Lists.list(true, false, true)),
                Arguments.of(false, Lists.list(false, false, false)),
                Arguments.of(true, Lists.list(true, true, true))
        );
    }

    @ParameterizedTest
    @MethodSource
    void support(boolean expected, ResolvableType resolvableType) {
        assertEquals(expected, merger.support(resolvableType));
    }

    static List<Arguments> support() {
        return Lists.list( // arguments:
                Arguments.of(false, ResolvableType.forClass(List.class)),
                Arguments.of(false, ResolvableType.forClass(Collection.class)),
                Arguments.of(false, ResolvableType.forClass(Integer.class)),
                Arguments.of(true, ResolvableType.forClass(Boolean.class)),
                Arguments.of(true, ResolvableType.forClass(Boolean.TYPE))
        );
    }
}