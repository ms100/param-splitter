package io.github.ms100.paramsplittersample.bean;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import lombok.Data;

import java.util.Collection;

/**
 * @author zhumengshuai
 */
@Data
public class Person {
    protected String name;

    @SplitParam(3)
    protected Collection<Integer> nums;

    protected Integer age;

}
