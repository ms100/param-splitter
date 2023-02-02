package io.github.ms100.paramsplitter.interceptor;

import io.github.ms100.paramsplitter.annotation.SplitParamParameterDetail;
import io.github.ms100.paramsplitter.merge.ResultMerger;
import io.github.ms100.paramsplitter.split.ParamSplitter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhumengshuai
 */
@Slf4j
class SplitParamOperation {
    @Getter
    private final Method method;

    private final SplitParamParameterDetail parameterDetail;

    private final ParamSplitter<Object> splitter;

    private final ResultMerger<Object> resultMerger;

    public SplitParamOperation(Method method, SplitParamParameterDetail parameterDetail,
                               ParamSplitter<Object> splitter, ResultMerger<Object> resultMerger) {
        this.method = method;
        this.parameterDetail = parameterDetail;
        this.splitter = splitter;
        this.resultMerger = resultMerger;
    }

    public int getSplitParamParameterPosition() {
        return parameterDetail.getPosition();
    }

    @Nullable
    public Object mergeResult(List<Object> resultChunks) {
        return resultMerger.merge(resultChunks);
    }

    public boolean needSplit(Object splitParamArg) {
        return splitter.needSplit(splitParamArg, parameterDetail.getAnnotation().chunkSize());
    }

    public List<?> splitArg(Object splitParamArg) {
        return splitter.split(splitParamArg, parameterDetail.getAnnotation().chunkSize());
    }

}

