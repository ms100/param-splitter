package io.github.ms100.paramsplitter.service;

import io.github.ms100.paramsplitter.SplitParamTest;
import io.github.ms100.paramsplitter.annotation.SplitParam;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Integer getStudent(@SplitParam(2) SplitParamTest.Student student, String str) {
        System.out.println("getStudent====----" + student.toString());
        return student.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getMan(@SplitParam(5) SplitParamTest.Man man, String str) {
        System.out.println("getMan====----" + man.toString());
        return man.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getWomen(@SplitParam SplitParamTest.Women women, String str) {
        System.out.println("getWomen====----" + women.toString());
        return women.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getTeacher(@SplitParam SplitParamTest.Teacher teacher, String a) {
        System.out.println("getTeacher====----" + teacher.toString());
        return teacher.getStudent().getNums().stream().mapToInt(Integer::valueOf).sum();
    }
}
