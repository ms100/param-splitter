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
class IntegerResultMergerTest {

    private SmartResultMerger<Integer> merger = new IntegerResultMerger();

    @ParameterizedTest
    @MethodSource
    void merge(int expected, List<Integer> list) {
        assertEquals(expected, merger.merge(list));
    }

    static List<Arguments> merge() {
        return Lists.list( // arguments:
                Arguments.of(6, Lists.list(1, 2, 3)),
                Arguments.of(15, Lists.list(4, 5, 6))
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
                Arguments.of(false, ResolvableType.forClass(Long.class)),
                Arguments.of(false, ResolvableType.forClass(Long.TYPE)),
                Arguments.of(false, ResolvableType.forClass(Boolean.class)),
                Arguments.of(true, ResolvableType.forClass(Integer.class)),
                Arguments.of(true, ResolvableType.forClass(Integer.TYPE))
        );
    }
}