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
class ArrayResultMergerTest {

    private final SmartResultMerger<Object[]> merger = new ArrayResultMerger<>();

    @ParameterizedTest
    @MethodSource
    void merge(Integer expected, List<Object[]> list) {
        assertEquals(expected, merger.merge(list).length);
    }

    static List<Arguments> merge() {
        return Lists.list( // arguments:
                Arguments.of(5, Lists.list(new Integer[]{1, 2}, new Integer[]{3, 4}, new Integer[]{5})),
                Arguments.of(9, Lists.list(new Integer[]{1, 2}, new Integer[]{3, 4, 5}, new Integer[]{6, 7, 8, 9}))
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
                Arguments.of(false, ResolvableType.forClass(Integer.class)),
                Arguments.of(false, ResolvableType.forClass(Integer.TYPE)),
                Arguments.of(true, ResolvableType.forClass(int[].class)),
                Arguments.of(true, ResolvableType.forClass(Object[].class)),
                Arguments.of(true, ResolvableType.forClass(Integer[].class))
        );
    }


    private final SmartResultMerger<int[]> merger2 = new ArrayResultMerger<>();

    @ParameterizedTest
    @MethodSource
    void merge2(int expected, List<int[]> list) {
        assertEquals(expected, merger2.merge(list).length);
    }

    static List<Arguments> merge2() {
        return Lists.list( // arguments:
                Arguments.of(5, Lists.list(new int[]{1, 2}, new int[]{3, 4}, new int[]{5})),
                Arguments.of(9, Lists.list(new int[]{1, 2}, new int[]{3, 4, 5}, new int[]{6, 7, 8, 9}))
        );
    }

    @ParameterizedTest
    @MethodSource
    void support2(boolean expected, ResolvableType resolvableType) {
        assertEquals(expected, merger2.support(resolvableType));
    }

    static List<Arguments> support2() {
        return Lists.list( // arguments:
                Arguments.of(false, ResolvableType.forClass(List.class)),
                Arguments.of(false, ResolvableType.forClass(Collection.class)),
                Arguments.of(false, ResolvableType.forClass(Long.class)),
                Arguments.of(false, ResolvableType.forClass(Long.TYPE)),
                Arguments.of(false, ResolvableType.forClass(Boolean.class)),
                Arguments.of(false, ResolvableType.forClass(Integer.class)),
                Arguments.of(false, ResolvableType.forClass(Integer.TYPE)),
                Arguments.of(true, ResolvableType.forClass(Object[].class)),
                Arguments.of(true, ResolvableType.forClass(int[].class))
        );
    }
}