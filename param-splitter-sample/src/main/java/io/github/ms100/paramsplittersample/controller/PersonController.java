package io.github.ms100.paramsplittersample.controller;

import io.github.ms100.paramsplittersample.bean.Man;
import io.github.ms100.paramsplittersample.bean.Student;
import io.github.ms100.paramsplittersample.bean.Teacher;
import io.github.ms100.paramsplittersample.bean.Women;
import io.github.ms100.paramsplittersample.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("getStudent")
    public Pair<?, ?> getStudent() {
        Student student = new Student();
        student.setName("张三");
        student.setAge(17);
        student.setNick("法外狂徒");
        student.setNums(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Integer res = personService.getStudent(student, "a");
        int expected = student.getNums().stream().mapToInt(Integer::valueOf).sum();
        return Pair.of(expected, res);
    }

    @GetMapping("getTeacher")
    public Pair<?, ?> getTeacher() {
        Student student = new Student();
        student.setName("张三");
        student.setAge(17);
        student.setNick("法外狂徒");
        student.setNums(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Teacher teacher = new Teacher();
        teacher.setName("赵六");
        teacher.setStudent(student);
        Integer res = personService.getTeacher(teacher, "a");
        int expected = student.getNums().stream().mapToInt(Integer::valueOf).sum();
        return Pair.of(expected, res);
    }

    @GetMapping("getMan")
    public Pair<?, ?> getMan() {
        Man man = new Man("李四");
        man.setAge(19);
        man.getNums().addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        Integer res = personService.getMan(man, "a");
        int expected = man.getNums().stream().mapToInt(Integer::valueOf).sum();
        return Pair.of(expected, res);
    }

    @GetMapping("getWomen")
    public Pair<?, ?> getWomen() {
        Women women = new Women("王五", Arrays.asList(1, 2, 3, 4, 5, 6, 7), 20);
        Integer res = personService.getWomen(women, "a");
        int expected = women.getNums().stream().mapToInt(Integer::valueOf).sum();
        return Pair.of(expected, res);
    }
}
