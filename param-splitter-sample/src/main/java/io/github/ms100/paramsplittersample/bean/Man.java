package io.github.ms100.paramsplittersample.bean;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhumengshuai
 */
@Data
@RequiredArgsConstructor
public class Man implements Cloneable {
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