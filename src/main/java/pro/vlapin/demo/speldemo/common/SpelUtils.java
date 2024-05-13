package pro.vlapin.demo.speldemo.common;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@UtilityClass
@ExtensionMethod(value = {
        TypeFactory.class,
        Objects.class,
}, suppressBaseMethods = false)
public class SpelUtils {

    public <T> T execute(@Language("SpEL") String self,
                         Class<? extends T> resultType) {
        return getParsedExpression(self)
                       .getValue(resultType)
                       .requireNonNull();
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(@Language("SpEL") String self,
                         ParameterizedTypeReference<? extends T> resultType) {
        return (T) execute(self, resultType.getType().rawClass());
    }

    //todo 13.05.2024: Check concurrency - is it possible to reuse this object with something like object pool?
    private static @NotNull Expression getParsedExpression(@Language("SpEL") String self) {
        return new SpelExpressionParser()
                       .parseExpression(self);
    }

    public <T> List<T> execute2List(@Language("SpEL") String self) {
        return execute(self, new ParameterizedTypeReference<>() { });
    }

    public <T> T executeWith(Object object,
                             @Language("SpEL") String spelExpression,
                             Class<? extends T> resultType) {
        return getParsedExpression(spelExpression)
                       .getValue(object, resultType)
                       .requireNonNull();
    }

    @SuppressWarnings("unchecked")
    public <T> T executeWith(Object object,
                             @Language("SpEL") String spelExpression,
                             ParameterizedTypeReference<? extends T> resultType) {
        return (T) executeWith(object, spelExpression, resultType.getType().rawClass());
    }
}
