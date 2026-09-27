package pro.vlapin.demo.speldemo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.NonFinal;
import lombok.val;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.vlapin.demo.speldemo.common.SpelUtils;

import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.assertj.core.api.Assertions.*;
import static pro.vlapin.demo.speldemo.SpelDemoApplicationTests.WritableInventor.*;
import static pro.vlapin.demo.speldemo.common.SpelUtils.*;

@ExtensionMethod(suppressBaseMethods = false, value = {
        SpelUtils.class,
})
class SpelDemoApplicationTests {

    record Inventor(String name, LocalDate birthday, String nationality, String[] inventionsArray) {
    }

    @Data(staticConstructor = "WritableInventor")
    static class WritableInventor {
        @NonFinal String name;
    }

    static Inventor TESLA = new Inventor("Nikola TESLA", LocalDate.of(1856, 7, 9),
                                         "Serbian",
                                         new String[] {"induction motor", "commutator for dynamo"});

    public static int add(int x, int y) {
        return x + y;
    }

    @Test
    @DisplayName("access works correctly")
    void accessWorksCorrectlyTest() {
        // given
        @Language("SpEL") String spelExpression = "inventionsArray[0].toUpperCase()";

        // when
        assertThat(SpelUtils.execute(spelExpression, TESLA, String.class)).isNotNull()
                                                                          // then
                                                                          .isEqualTo("induction motor".toUpperCase());
    }

    @Test
    @DisplayName("array literals works correctly")
    void arrayLiteralsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val intArrayInitialization = "new int[] { 1,2,3,4} ";
        val array = execute(intArrayInitialization, int[].class);
        // when
        assertThat(array.length)
                // then
                .isEqualTo(4);
    }

    @Test
    @DisplayName("literal expression works correctly")
    void literalExpressionWorksCorrectlyTest() {
        // given, when
        @Language("SpEL") String getStringBytes = "'hello world'.concat('!').bytes";
        val bytesWithLombok = getStringBytes.execute(byte[].class);
        val bytesWithoutLombok = execute(getStringBytes, byte[].class);

        // then
        assertThat(new String(bytesWithLombok)).isNotNull().isEqualTo("hello world!");
        assertThat(new String(bytesWithoutLombok)).isNotNull().isEqualTo("hello world!");
    }

    @Test
    @DisplayName("list literals works correctly")
    void listLiteralsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val numberedListExpression = "{ 0, 1, 2 , 3 }";

        // when
        List<Integer> numberedList = execute(numberedListExpression);
        // then
        assertThat(numberedList).isNotNull()
                                .isNotEmpty()
                                .hasSize(4)
                                .containsExactly(0, 1, 2, 3);

        // when
        List<Integer> intsWithLombok = numberedListExpression.execute();
        // then
        assertThat(intsWithLombok).isNotNull().isNotEmpty().hasSize(4).containsExactly(0, 1, 2, 3);

        // when
        List<Integer> intsWithoutLombok = execute(numberedListExpression);
        // then
        assertThat(intsWithoutLombok).isNotNull().isNotEmpty().hasSize(4).containsExactly(0, 1, 2, 3);
    }

    @Test
    @DisplayName("map literals works correctly")
    void mapLiteralsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val mapExpression = "{ name: 'Bob'}";

        // when
        Map<String, String> map = execute(mapExpression);

        // then
        assertThat(map).isNotNull()
                       .containsKey("name")
                       .containsExactly(Map.entry("name", "Bob"));
    }

    @Test
    @DisplayName("methods works correctly")
    void methodsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val methodCallExpression = " 'abc'.substring(1,3) ";

        // when
        String subStringOutput = execute(methodCallExpression);

        // then
        assertThat(subStringOutput).isNotNull()
                                   .isEqualTo("bc");
    }

    @Test
    @DisplayName("operators works correctly")
    void operatorsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val operator2Equals2 = "2 == 2";
        @Language("SpEL") val operatorLaterThen = "'black' < 'block'";

        // when
        boolean isTwoEqualsTwo = execute(operator2Equals2);
        boolean isComparisonValid = execute(operatorLaterThen);

        // then
        assertThat(isTwoEqualsTwo).isTrue();
        assertThat(isComparisonValid).isTrue();
    }

    @Test
    @DisplayName("types works correctly")
    void typesWorksCorrectlyTest() {
        // given
        @Language("SpEL") val constantReadExpression = "T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR";

        // when
        boolean isCeilingLessThanFloor = execute(constantReadExpression);

        // then
        assertThat(isCeilingLessThanFloor).isTrue();
    }

    @Test
    @DisplayName("variables works correctly")
    void variablesWorksCorrectlyTest() {
        // given
        val readWriteContext = SimpleEvaluationContext.forReadWriteDataBinding().build();
        readWriteContext.setVariable("newName", "Mike");

        @Language("SpEL") val variableWriterExpression = "name = #newName";

        val writableInventor = WritableInventor().setName("Nick");
        assertThat(writableInventor.getName()).isEqualTo("Nick");

        // when
        execute(variableWriterExpression, readWriteContext, writableInventor);

        // then
        assertThat(writableInventor.getName()).isNotNull().isEqualTo("Mike");
    }

    @Test
    @SneakyThrows
    @DisplayName("function call works correctly")
    void functionCallWorksCorrectlyTest() {
        // given
        val addMethod = getClass().getMethod("add", int.class, int.class);
        assertThat(addMethod).isNotNull()
                             .extracting(Method::getModifiers)
                             .matches(Modifier::isStatic); // only static methods are allowed!

        val spelContext = new StandardEvaluationContext();
        spelContext.setVariable("x", 10);
        spelContext.setVariable("y", 5);
        spelContext.registerFunction("add", addMethod);

        @Language("SpEL") val addExpression = "#x + #y";
        @Language("SpEL") val addMethodCall = "#add(#x, #y)";

        // when
        int exprResult = execute(addExpression, spelContext);
        int methodCallResult = execute(addMethodCall, spelContext);

        // then
        assertThat(exprResult).isEqualTo(15);
        assertThat(methodCallResult).isEqualTo(15);
    }

    @Test
    @DisplayName("Simple evaluation context without instance method works correctly")
    void simpleEvaluationContextWithoutInstanceMethodWorksCorrectlyTest() {
        // given
        val builder = SimpleEvaluationContext.forReadOnlyDataBinding().withRootObject(TESLA);
        @Language("SpEL") val getNameExpression = "#root.name"; // .getValue(builder.build(), String.class);
        @Language("SpEL") val toStringCallExpression = "#root.toString()";

        // when, then

        String name = execute(getNameExpression, builder.build());
        assertThat(name).isNotNull().isEqualTo("Nikola TESLA");

        assertThatThrownBy(() -> execute(toStringCallExpression, builder.build()))
                .isInstanceOf(SpelEvaluationException.class)
                .hasMessage("EL1004E: Method call: Method toString() cannot be found on type pro.vlapin.demo.speldemo.SpelDemoApplicationTests$Inventor");

        String string = execute(toStringCallExpression, builder.withInstanceMethods().build());
        assertThat(string).isNotNull().startsWith("Inventor[name=Nikola TESLA, birthday=1856-07-09, nationality=Serbian, inventionsArray=[");
    }

    @Test
    @DisplayName("Root object works correctly")
    void rootObjectWorksCorrectlyTest() {
        // given
        @Language("SpEL") val nameExpression = "name";
        @Language("SpEL") val isNameEqNikolaTeslaExpression = "name == 'Nikola TESLA'";

        // when
        String name = execute(nameExpression, TESLA);
        boolean isNameEqNikolaTesla = execute(isNameEqNikolaTeslaExpression, TESLA);

        // then
        assertThat(name).isNotNull().isEqualTo("Nikola TESLA");
        assertThat(isNameEqNikolaTesla).isTrue();
    }

    @Test
    @DisplayName("selection works correctly")
    void selectionWorksCorrectlyTest() {
        // given
        record Cat(String type) { }

        val cats = List.of(
                new Cat("Leopard"),
                new Cat("Tiger"),
                new Cat("Lion"),
                new Cat("Tiger"));

        @Language("SpEL") val fewerValuesExpression = "#root.?[ type == 'Tiger' ]";
        
        // when
        List<Cat> fewerValues = execute(fewerValuesExpression, cats);

        // then
        assertThat(fewerValues).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    @DisplayName("navigations works correctly")
    void navigationsWorksCorrectlyTest() {
        // given
        val inventor = new Inventor(null, null, null, null);
        @Language("SpEL") val nameExpression = "name ?: 'Bob'";
        @Language("SpEL") val arrayLengthExpression = "inventionsArray?.length";

        // when
        String name = execute(nameExpression, inventor);
        Optional<Integer> length = executeOpt(arrayLengthExpression, inventor);

        // then
        assertThat(name).isNotNull().isEqualTo("Bob");
        assertThat(length).isNotNull().isEmpty();
    }
}
