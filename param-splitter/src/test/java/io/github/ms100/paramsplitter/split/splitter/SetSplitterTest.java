package io.github.ms100.paramsplitter.split.splitter;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SetSplitterTest {

    private final SetParamSplitter splitter = new SetParamSplitter();

    @Test
    void split() {
        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        List<Set<?>> split = splitter.split(set, 2);
        System.out.println(split);
        assertEquals(split.get(0).size(), 2);
        assertTrue(split.get(0).contains(1));
        assertTrue(split.get(0).contains(2));
        assertTrue(split.get(1).contains(3));
        assertTrue(split.get(1).contains(4));
        assertTrue(split.get(2).contains(5));
    }

    @Test
    void support() {
        assertTrue(splitter.support(ResolvableType.forClass(Set.class)));
        assertFalse(splitter.support(ResolvableType.forClass(List.class)));
    }
}