package io.github.ms100.paramsplittersample.service;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import io.github.ms100.paramsplittersample.bean.Man;
import io.github.ms100.paramsplittersample.bean.Student;
import io.github.ms100.paramsplittersample.bean.Teacher;
import io.github.ms100.paramsplittersample.bean.Women;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhumengshuai
 */
@Service
@Slf4j
public class PersonService {

    public Integer getStudent(@SplitParam(2) Student student, String str) {
        log.info("getStudent====----{}", student.toString());
        return student.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getMan(@SplitParam(5) Man man, String str) {
        log.info("getMan====----{}", man.toString());
        return man.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getWomen(@SplitParam Women women, String str) {
        log.info("getWomen====----{}", women.toString());
        return women.getNums().stream().mapToInt(Integer::valueOf).sum();
    }

    public Integer getTeacher(@SplitParam Teacher teacher, String a) {
        log.info("getTeacher====----{}", teacher.toString());
        return teacher.getStudent().getNums().stream().mapToInt(Integer::valueOf).sum();
    }

}
