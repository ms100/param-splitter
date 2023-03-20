package io.github.ms100.paramsplitter.config;

import io.github.ms100.paramsplitter.annotation.EnableParamSplitter;
import io.github.ms100.paramsplitter.interceptor.SplitParamInterceptor;
import io.github.ms100.paramsplitter.interceptor.SplitParamOperationSource;
import io.github.ms100.paramsplitter.interceptor.SplitParamOperationSourcePointcut;
import io.github.ms100.paramsplitter.merge.ResultMergerRegistry;
import io.github.ms100.paramsplitter.merge.ResultMergerResolver;
import io.github.ms100.paramsplitter.merge.SmartResultMerger;
import io.github.ms100.paramsplitter.split.ParamSplitterRegistry;
import io.github.ms100.paramsplitter.split.ParamSplitterResolver;
import io.github.ms100.paramsplitter.split.SmartParamSplitter;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author zhumengshuai
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ParamSplitterConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableParamSplitter.class.getName()));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableParamSplitter is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public PointcutAdvisor splitParamOperationSourceAdvisor(
            SplitParamOperationSource operationSource, SplitParamInterceptor interceptor) {

        DefaultPointcutAdvisor advisor =
                new DefaultPointcutAdvisor(new SplitParamOperationSourcePointcut(operationSource), interceptor);

        if (this.annotationAttributes != null) {
            advisor.setOrder(this.annotationAttributes.<Integer>getNumber("order"));
        }

        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SplitParamOperationSource splitParamOperationSource(
            ParamSplitterRegistry paramSplitterRegistry, ResultMergerRegistry resultMergerRegistry) {

        return new SplitParamOperationSource(paramSplitterRegistry, resultMergerRegistry);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ParamSplitterRegistry paramSplitterService(List<ParamSplitterResolver> splitterResolvers, List<SmartParamSplitter<?>> smartParamSplitters) {
        return new ParamSplitterRegistry(splitterResolvers, smartParamSplitters);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ResultMergerRegistry resultMergerService(List<ResultMergerResolver> mergerResolvers, List<SmartResultMerger<?>> smartResultMergers) {
        return new ResultMergerRegistry(mergerResolvers, smartResultMergers);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SplitParamInterceptor splitParamInterceptor(SplitParamOperationSource splitParamOperationSource) {
        return new SplitParamInterceptor(splitParamOperationSource);
    }
}
