package io.github.ms100.paramsplitter.interceptor;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author zhumengshuai
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class SplitParamOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

	@Nullable
	private final SplitParamOperationSource operationSource;

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return (operationSource != null && operationSource.getOperation(method, targetClass) != null);
	}
}
