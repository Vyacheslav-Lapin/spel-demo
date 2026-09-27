package pro.vlapin.demo.speldemo.common;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.type.TypeFactory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@UtilityClass
@ExtensionMethod(suppressBaseMethods = false, value = {
        TypeFactory.class,
        Objects.class,
        Optional.class,
})
public class SpelUtils {

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    // region SpEL expression
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Class<T> resultType) {
        return parseExpression(spelExpression)
                       .getValue(resultType)
                       .ofNullable();
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Class<T> resultType) {
        return executeOpt(spelExpression, resultType)
                       .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Type resultType) {
        return executeOpt(spelExpression, (Class<T>) resultType.rawClass());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Type resultType) {
        return SpelUtils.<T>executeOpt(spelExpression, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String self) {
        return executeOpt(self, new TypeReference<>() { });
    }

    public <T> T execute(@Language("SpEL") String self) {
        return SpelUtils.<T>executeOpt(self)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, resultType)
                       .orElseThrow();
    }
    // endregion

    // region SpEL expression + rootObject
    private static <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Object rootObject, Class<T> resultType) {
        return parseExpression(spelExpression)
                       .getValue(rootObject, resultType)
                       .ofNullable();
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Object rootObject, Class<T> resultType) {
        return executeOpt(spelExpression, rootObject, resultType)
                       .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Object rootObject, Type resultType) {
        return executeOpt(spelExpression, rootObject, (Class<T>) resultType.rawClass());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Object rootObject, Type resultType) {
        return SpelUtils.<T>executeOpt(spelExpression, rootObject, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Object rootObject, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, rootObject, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Object rootObject, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, rootObject, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Object rootObject) {
        return executeOpt(spelExpression, rootObject, new TypeReference<>() { });
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Object rootObject) {
        return SpelUtils.<T>executeOpt(spelExpression, rootObject).orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, Object rootObject, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, rootObject, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, Object rootObject, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, rootObject, resultType).orElseThrow();
    }
    // endregion

    // region SpEL expression + context
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Class<T> resultType) {
        return parseExpression(spelExpression)
                       .getValue(context, resultType)
                       .ofNullable();
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Class<T> resultType) {
        return executeOpt(spelExpression, context, resultType)
                       .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Type resultType) {
        return executeOpt(spelExpression, context, (Class<T>) resultType.rawClass());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Type resultType) {
        return SpelUtils.<T>executeOpt(spelExpression, context, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context) {
        return executeOpt(spelExpression, context, new TypeReference<>() { });
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context) {
        return SpelUtils.<T>executeOpt(spelExpression, context)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, resultType)
                       .orElseThrow();
    }
    // endregion

    // region SpEL expression + context + rootObject
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, Class<T> resultType) {
        return parseExpression(spelExpression)
                       .getValue(context, rootObject, resultType)
                       .ofNullable();
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, Class<T> resultType) {
        return executeOpt(spelExpression, context, rootObject, resultType)
                       .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, Type resultType) {
        return executeOpt(spelExpression, context, rootObject, (Class<T>) resultType.rawClass());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, Type resultType) {
        return SpelUtils.<T>executeOpt(spelExpression, context, rootObject, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, TypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, rootObject, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, TypeReference<? extends T> resultType) {
        return SpelUtils.<T>executeOpt(spelExpression, context, rootObject, resultType)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject) {
        return executeOpt(spelExpression, context, rootObject, new TypeReference<>() {});
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject) {
        return SpelUtils.<T>executeOpt(spelExpression, context, rootObject)
                       .orElseThrow();
    }

    public <T> Optional<T> executeOpt(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, rootObject, resultType.getType());
    }

    public <T> T execute(@Language("SpEL") String spelExpression, EvaluationContext context, Object rootObject, ParameterizedTypeReference<? extends T> resultType) {
        return executeOpt(spelExpression, context, rootObject, resultType)
                       .orElseThrow();
    }
    // endregion

    private static @NotNull Expression parseExpression(@Language("SpEL") String self) {
        return spelExpressionParser()
                       .parseExpression(self);
    }
}
