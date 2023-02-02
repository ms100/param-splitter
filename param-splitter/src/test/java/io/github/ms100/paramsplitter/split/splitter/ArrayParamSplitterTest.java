package io.github.ms100.paramsplitter.split.splitter;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArrayParamSplitterTest {

    private final ArrayParamSplitter<int[]> splitter = new ArrayParamSplitter<>();

    @Test
    void needSplit() {
        assertTrue(splitter.needSplit(new int[]{1, 2, 3}, 2));
        assertFalse(splitter.needSplit(new int[]{1, 2, 3}, 3));
        assertFalse(splitter.needSplit(new int[]{1, 2, 3}, 4));
    }

    @Test
    void split() {
        int[] array = new int[]{1, 2, 3, 4, 5};
        List<int[]> split = splitter.split(array, 2);
        split.forEach(o -> System.out.println(Arrays.toString(o)));
        assertEquals(split.get(0).length, 2);
        assertEquals(1, split.get(0)[0]);
        assertEquals(2, split.get(0)[1]);
        assertEquals(3, split.get(1)[0]);
        assertEquals(4, split.get(1)[1]);
        assertEquals(5, split.get(2)[0]);
    }

    @Test
    void support() {
        assertTrue(splitter.support(ResolvableType.forClass(String[].class)));
        assertTrue(splitter.support(ResolvableType.forClass(Integer[].class)));
        assertTrue(splitter.support(ResolvableType.forClass(int[].class)));
        assertTrue(splitter.support(ResolvableType.forClass(long[].class)));
        assertFalse(splitter.support(ResolvableType.forClass(Collection.class)));
        assertFalse(splitter.support(ResolvableType.forClass(List.class)));
        assertFalse(splitter.support(ResolvableType.forClass(Set.class)));
    }

    private final ArrayParamSplitter<long[]> splitter2 = new ArrayParamSplitter<>();

    @Test
    void needSplit2() {
        assertTrue(splitter2.needSplit(new long[]{1, 2, 3}, 2));
        assertFalse(splitter2.needSplit(new long[]{1, 2, 3}, 3));
        assertFalse(splitter2.needSplit(new long[]{1, 2, 3}, 4));
    }

    @Test
    void split2() {
        long[] array = new long[]{1, 2, 3, 4, 5};
        List<long[]> split = splitter2.split(array, 2);
        split.forEach(o -> System.out.println(Arrays.toString(o)));
        assertEquals(split.get(0).length, 2);
        assertEquals(1, split.get(0)[0]);
        assertEquals(2, split.get(0)[1]);
        assertEquals(3, split.get(1)[0]);
        assertEquals(4, split.get(1)[1]);
        assertEquals(5, split.get(2)[0]);
    }

    @Test
    void support2() {
        assertTrue(splitter2.support(ResolvableType.forClass(String[].class)));
        assertTrue(splitter2.support(ResolvableType.forClass(Integer[].class)));
        assertTrue(splitter2.support(ResolvableType.forClass(int[].class)));
        assertTrue(splitter2.support(ResolvableType.forClass(long[].class)));
        assertFalse(splitter2.support(ResolvableType.forClass(Collection.class)));
        assertFalse(splitter2.support(ResolvableType.forClass(List.class)));
        assertFalse(splitter2.support(ResolvableType.forClass(Set.class)));
    }


    private final ArrayParamSplitter<Object[]> splitter3 = new ArrayParamSplitter<>();

    @Test
    void needSplit3() {
        assertTrue(splitter3.needSplit(new Integer[]{1, 2, 3}, 2));
        assertFalse(splitter3.needSplit(new Integer[]{1, 2, 3}, 3));
        assertFalse(splitter3.needSplit(new Integer[]{1, 2, 3}, 4));
    }

    @Test
    void split3() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5};
        List<Object[]> split = splitter3.split(array, 2);
        split.forEach(o -> System.out.println(Arrays.toString(o)));
        assertEquals(split.get(0).length, 2);
        assertEquals(1, split.get(0)[0]);
        assertEquals(2, split.get(0)[1]);
        assertEquals(3, split.get(1)[0]);
        assertEquals(4, split.get(1)[1]);
        assertEquals(5, split.get(2)[0]);
    }

    @Test
    void support3() {
        assertTrue(splitter3.support(ResolvableType.forClass(String[].class)));
        assertTrue(splitter3.support(ResolvableType.forClass(Integer[].class)));
        assertTrue(splitter3.support(ResolvableType.forClass(int[].class)));
        assertTrue(splitter3.support(ResolvableType.forClass(long[].class)));
        assertFalse(splitter3.support(ResolvableType.forClass(Collection.class)));
        assertFalse(splitter3.support(ResolvableType.forClass(List.class)));
        assertFalse(splitter3.support(ResolvableType.forClass(Set.class)));
    }
}