package io.github.ms100.paramsplitter.split.splitter;

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CollectionParamSplitterTest {

    private final CollectionParamSplitter splitter = new CollectionParamSplitter();

    @Test
    void needSplit() {
        assertTrue(splitter.needSplit(Arrays.asList(1, 2, 3), 2));
        assertFalse(splitter.needSplit(Arrays.asList(1, 2, 3), 3));
    }

    @Test
    void split() {
        Collection<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        List<Collection<?>> split = splitter.split(list, 2);
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
        assertTrue(splitter.support(ResolvableType.forClass(Collection.class)));
        assertTrue(splitter.support(ResolvableType.forClass(List.class)));
        assertFalse(splitter.support(ResolvableType.forClass(Set.class)));
    }
}