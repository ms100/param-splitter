package io.github.ms100.paramsplittersample.bean;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhumengshuai
 */
@Data
@RequiredArgsConstructor
public class Women {
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