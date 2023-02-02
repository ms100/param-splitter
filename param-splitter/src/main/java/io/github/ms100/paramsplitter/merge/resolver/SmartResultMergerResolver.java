package io.github.ms100.paramsplitter.merge.resolver;

import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.merge.ResultMergerResolver;
import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumengshuai
 */
public class SmartResultMergerResolver implements ResultMergerResolver {

    private final List<SmartResultMerger<?>> resultMergers = new ArrayList<>();
    
    public void addMergers(List<SmartResultMerger<?>> resultMergers) {
        this.resultMergers.addAll(resultMergers);
    }

    @Override
    public ResultMerger<Object> resolve(ResolvableType resolvableType) {
        for (SmartResultMerger<?> resultMerger : resultMergers) {
            if (resultMerger.support(resolvableType)) {
                return (ResultMerger<Object>) resultMerger;
            }
        }

        return null;
    }
}
