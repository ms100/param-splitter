package io.github.ms100.paramsplittersample.bean;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import lombok.Data;

/**
 * @author zhumengshuai
 */
@Data
public class Teacher {
    protected String name;

    @SplitParam(4)
    protected Student student;
}
