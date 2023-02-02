package io.github.ms100.paramsplitter.split.resolver;

import io.github.ms100.paramsplitter.annotation.SplitParam;
import io.github.ms100.paramsplitter.split.ParamSplitter;
import io.github.ms100.paramsplitter.split.ParamSplitterRegistry;
import io.github.ms100.paramsplitter.split.ParamSplitterRegistryAware;
import io.github.ms100.paramsplitter.split.ParamSplitterResolver;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhumengshuai
 */
public class AnnotatedParamSplitterResolver implements ParamSplitterResolver, ParamSplitterRegistryAware {

    @Nullable
    private ParamSplitterRegistry paramSplitterRegistry;

    private final ConcurrentMap<Class<?>, AnnotatedParamSplitter> cache = new ConcurrentHashMap<>(256);

    @Override
    public void setParamSplitterRegistry(ParamSplitterRegistry paramSplitterRegistry) {
        this.paramSplitterRegistry = paramSplitterRegistry;
    }

    @Nullable
    @Override
    public ParamSplitter<Object> resolve(ResolvableType resolvableType) {
        Class<?> targetClass = resolvableType.resolve();
        if (targetClass == null || !AnnotationUtils.isCandidateClass(targetClass, SplitParam.class)) {
            return null;
        }

        return cache.computeIfAbsent(targetClass, clazz -> {
            List<Field> allFieldsList = FieldUtils.getAllFieldsList(clazz);
            List<Field> annotatedFields = allFieldsList.stream().filter(f -> f.isAnnotationPresent(SplitParam.class))
                    .collect(Collectors.toList());

            if (annotatedFields.size() == 0) {
                return null;
            } else if (annotatedFields.size() > 1) {
                throw new IllegalStateException("More than one field has @SplitParam on " + clazz);
            }

            Field annotatedField = annotatedFields.get(0);
            ResolvableType fieldResolvableType = ResolvableType.forField(annotatedField);
            assert paramSplitterRegistry != null;
            ParamSplitter<Object> fieldParamSplitter = paramSplitterRegistry.getSplitter(fieldResolvableType);
            if (fieldParamSplitter == null) {
                return null;
            }

            Function<Object, Object> copyFunction;
            if (Cloneable.class.isAssignableFrom(clazz)) {

                Method cloneMethod;
                try {
                    cloneMethod = clazz.getMethod("clone");
                    ReflectionUtils.makeAccessible(cloneMethod);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                copyFunction = src -> {
                    try {
                        return cloneMethod.invoke(src);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            } else {
                Constructor<?> constructor;
                try {
                    constructor = ReflectionUtils.accessibleConstructor(clazz);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("Class " + clazz + " need to implements Cloneable or has no args construct method");
                }
                final List<Field> copyFieldsList = allFieldsList.stream()
                        .filter(f -> !Modifier.isStatic(f.getModifiers()))
                        .peek(ReflectionUtils::makeAccessible).collect(Collectors.toList());

                copyFunction = source -> {
                    Object destination;
                    try {
                        destination = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    copyFieldsList.forEach(field -> {
                        try {
                            Object srcValue = field.get(source);
                            field.set(destination, srcValue);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return destination;
                };
            }

            return new AnnotatedParamSplitter(annotatedField, copyFunction, fieldParamSplitter);
        });
    }

    static class AnnotatedParamSplitter implements ParamSplitter<Object> {

        private final Field field;

        private final Function<Object, Object> copyFunction;

        private final ParamSplitter<Object> fieldParamSplitter;

        private final int defaultChunkSize;

        @SneakyThrows
        public AnnotatedParamSplitter(Field field, Function<Object, Object> copyFunction, ParamSplitter<Object> fieldParamSplitter) {
            ReflectionUtils.makeAccessible(field);
            this.field = field;
            this.copyFunction = copyFunction;
            this.fieldParamSplitter = fieldParamSplitter;
            this.defaultChunkSize = Objects.requireNonNull(AnnotationUtils.getAnnotation(field, SplitParam.class)).chunkSize();
        }

        @SneakyThrows
        @Override
        public boolean needSplit(Object splitParamArg, int chunkSize) {
            if (chunkSize == Integer.MAX_VALUE) {
                chunkSize = defaultChunkSize;
            }
            Object fieldArg = field.get(splitParamArg);
            return fieldParamSplitter.needSplit(fieldArg, chunkSize);
        }

        @SneakyThrows
        @Override
        public List<Object> split(Object splitParamArg, int chunkSize) {
            if (chunkSize == Integer.MAX_VALUE) {
                chunkSize = defaultChunkSize;
            }
            Object fieldArg = field.get(splitParamArg);
            List<Object> fieldArgChunks = fieldParamSplitter.split(fieldArg, chunkSize);
            List<Object> list = new ArrayList<>(fieldArgChunks.size());

            for (Object fieldArgChunk : fieldArgChunks) {
                Object copy = copyFunction.apply(splitParamArg);
                field.set(copy, fieldArgChunk);
                list.add(copy);
            }
            return list;
        }
    }
}
