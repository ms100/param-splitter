package io.github.ms100.paramsplitter.split.resolver;

import io.github.ms100.paramsplitter.annotation.EnableParamSplitter;
import io.github.ms100.paramsplitter.annotation.SplitParam;
import io.github.ms100.paramsplitter.split.ParamSplitter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableParamSplitter
class AnnotatedParamSplitterResolverTest {

    @Autowired
    private AnnotatedParamSplitterResolver resolver;

    @Test
    void resolve() {
        assertNotNull(resolver.resolve(ResolvableType.forClass(Person.class)));
        assertNotNull(resolver.resolve(ResolvableType.forClass(Student.class)));
        assertNull(resolver.resolve(ResolvableType.forClass(Object.class)));
        assertNull(resolver.resolve(ResolvableType.forClass(Teacher.class)));
    }

    @Test
    void split() {
        ParamSplitter<Object> resolve = Objects.requireNonNull(resolver.resolve(ResolvableType.forClass(Student.class)));
        Student student = new Student();
        student.name = "张三";
        student.age = 17;
        student.nick = "法外狂徒";
        student.nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Object> split = resolve.split(student, 2);
        assertEquals(4, split.size());
        System.out.println(split);
    }

    @Test
    void splitMan() {
        ParamSplitter<Object> resolve = Objects.requireNonNull(resolver.resolve(ResolvableType.forClass(Man.class)));
        Man man = new Man("李四");
        man.age = 19;
        man.nums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        List<Object> split = resolve.split(man, 5);
        assertEquals(2, split.size());
        System.out.println(split);
    }

    @Test
    void splitWomen() {
        ParamSplitter<Object> resolve = Objects.requireNonNull(resolver.resolve(ResolvableType.forClass(Women.class)));
        Women women = new Women("王五", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 20);
        List<Object> split = resolve.split(women, Integer.MAX_VALUE);
        assertEquals(3, split.size());
        System.out.println(split);
    }

    @ToString
    static class Person {
        protected String name;

        @SplitParam(3)
        protected Collection<?> nums;

        protected Integer age;

    }

    @ToString(callSuper = true)
    static class Student extends Person {
        protected String nick;
    }

    static class Teacher {

    }

    @ToString
    @RequiredArgsConstructor
    static class Man implements Cloneable {
        protected final String name;

        @SplitParam(3)
        protected final Collection<Integer> nums = new ArrayList<>();

        protected Integer age;

        @SneakyThrows
        @Override
        public Man clone() {
            return (Man) super.clone();
        }

    }

    @ToString
    @RequiredArgsConstructor
    static class Women {
        protected final String name;

        @SplitParam(3)
        protected final Collection<Integer> nums;

        protected final Integer age;

        public Women() {
            name = "";
            nums = new ArrayList<>();
            age = 0;
        }
    }
}