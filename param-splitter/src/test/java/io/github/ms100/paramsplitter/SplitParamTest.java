package io.github.ms100.paramsplitter;

import io.github.ms100.paramsplitter.annotation.EnableParamSplitter;
import io.github.ms100.paramsplitter.annotation.SplitParam;
import io.github.ms100.paramsplitter.service.FoxService;
import io.github.ms100.paramsplitter.service.PersonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableParamSplitter
public class SplitParamTest {

    @Autowired
    FoxService foxService;

    @Autowired
    PersonService personService;

    private final List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 5);


    @Test
    void getFox() {
        int[] idIntArray = ids.stream().mapToInt(Integer::valueOf).toArray();
        Integer[] idIntegerArray = ids.toArray(new Integer[0]);
        List<String> foxList = foxService.getFoxList(ids, "a");
        System.out.println(foxList);
        assertEquals(ids.size(), foxList.size());
        Map<Integer, String> foxMap = foxService.getFoxMap(ids, "a");
        System.out.println(foxMap);
        assertEquals(ids.stream().distinct().count(), foxMap.size());
        System.out.println(foxService.getFoxMap(new HashSet<>(ids), "a"));
        foxService.putFoxList(ids, "a");
        foxService.putFoxSet(new HashSet<>(ids), "a");
        boolean foxBoolean = foxService.getFoxBoolean(idIntegerArray, "a");
        System.out.println(foxBoolean);
        assertTrue(foxBoolean);
        int foxInt = foxService.getFoxInt(idIntArray, "a");
        System.out.println(foxInt);
        assertEquals(ids.stream().mapToInt(Integer::valueOf).sum(), foxInt);
        Object[] foxObjectArray = foxService.getFoxObjectArray(idIntegerArray, "a");
        System.out.println(Arrays.toString(foxObjectArray));
        assertArrayEquals(idIntegerArray, foxObjectArray);
        int[] foxIntArray = foxService.getFoxIntArray(idIntArray, "a");
        System.out.println(Arrays.toString(foxIntArray));
        assertArrayEquals(idIntArray, foxIntArray);
    }

    @Test
    void getFoxCF() {
        CompletableFuture<List<String>> cf1 = foxService.getFoxListCF(ids, "a");
        System.out.println("cf1 join");
        System.out.println(cf1.join());
        CompletableFuture<Map<Integer, String>> cf2 = foxService.getFoxMapCF(new HashSet<>(ids), "a");
        System.out.println("cf2 join");
        System.out.println(cf2.join());
        CompletableFuture<Set<String>> cf3 = foxService.getFoxSetCF(ids, "a");
        System.out.println("cf3 join");
        System.out.println(cf3.join());
        CompletableFuture<Void> cf4 = foxService.putFoxListCF(ids, "a");
        System.out.println("cf4 join");
        cf4.join();
        CompletableFuture<Void> cf5 = foxService.putFoxSetCF(new HashSet<>(ids), "a");
        System.out.println("cf5 join");
        cf5.join();
        CompletableFuture<Boolean> cf6 = foxService.getFoxBooleanCF(ids, "a");
        System.out.println("cf6 join");
        System.out.println(cf6.join());
    }

    @Test
    void getFoxMono() {
        Mono<List<String>> mono1 = foxService.getFoxListMono(ids, "a");
        System.out.println("mono1 block");
        System.out.println(mono1.block());
        Mono<Map<Integer, String>> mono2 = foxService.getFoxMapMono(new HashSet<>(ids), "a");
        System.out.println("mono2 block");
        System.out.println(mono2.block());
        Mono<Set<String>> mono3 = foxService.getFoxSetMono(ids, "a");
        System.out.println("mono3 block");
        System.out.println(mono3.block());
        Mono<Void> mono4 = foxService.putFoxListMono(ids, "a");
        System.out.println("mono4 block");
        mono4.block();
        Mono<Void> mono5 = foxService.putFoxSetMono(new HashSet<>(ids), "a");
        System.out.println("mono5 block");
        mono5.block();
    }

    @Test
    void getStudent() {
        Student student = new Student();
        student.name = "张三";
        student.age = 17;
        student.nick = "法外狂徒";
        student.nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Integer res = personService.getStudent(student, "a");
        int expected = student.nums.stream().mapToInt(Integer::valueOf).sum();
        assertEquals(expected, res);
    }

    @Test
    void getTeacher() {
        Student student = new Student();
        student.name = "张三";
        student.age = 17;
        student.nick = "法外狂徒";
        student.nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Teacher teacher = new Teacher();
        teacher.name = "赵六";
        teacher.student = student;
        Integer res = personService.getTeacher(teacher, "a");
        int expected = student.nums.stream().mapToInt(Integer::valueOf).sum();
        assertEquals(expected, res);
    }

    @Test
    void getMan() {
        Man man = new Man("李四");
        man.age = 19;
        man.nums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        Integer res = personService.getMan(man, "a");
        int expected = man.nums.stream().mapToInt(Integer::valueOf).sum();
        assertEquals(expected, res);
    }

    @Test
    void getWomen() {
        Women women = new Women("王五", Arrays.asList(1, 2, 3, 4, 5, 6, 7), 20);
        Integer res = personService.getWomen(women, "a");
        int expected = women.nums.stream().mapToInt(Integer::valueOf).sum();
        assertEquals(expected, res);
    }

    @ToString
    static class Person {
        protected String name;

        @SplitParam(3)
        @Getter
        protected Collection<Integer> nums;

        protected Integer age;

    }

    @ToString(callSuper = true)
    public static class Student extends Person {
        protected String nick;
    }

    @ToString
    public static class Teacher {
        protected String name;

        @Getter
        @SplitParam(4)
        protected Student student;
    }

    @ToString
    @RequiredArgsConstructor
    public static class Man implements Cloneable {
        protected final String name;

        @SplitParam(3)
        @Getter
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
    public static class Women {
        protected final String name;

        @SplitParam(3)
        @Getter
        protected final Collection<Integer> nums;

        protected final Integer age;

        public Women() {
            name = "";
            nums = new ArrayList<>();
            age = 0;
        }
    }
}
