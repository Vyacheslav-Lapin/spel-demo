package pro.vlapin.demo.speldemo;

import lombok.experimental.ExtensionMethod;
import lombok.val;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.vlapin.demo.speldemo.common.SpelUtils;

import static org.assertj.core.api.Assertions.*;

@ExtensionMethod(value = {
        SpelUtils.class,
}, suppressBaseMethods = false)
class SpelDemoApplicationTests {

    @Test
    @DisplayName("list literals works correctly")
    void listLiteralsWorksCorrectlyTest() {
        // given
        @Language("SpEL") val intListExpression = "{ 0, 1, 2, 3 }";

        // when
        val intsWithLombok = intListExpression.<Integer>execute2List();
        val intsWithoutLombok = SpelUtils.<Integer>execute2List(intListExpression);

        // then
        assertThat(intsWithLombok).isNotNull().isNotEmpty().containsExactly(0, 1, 2, 3);
        assertThat(intsWithoutLombok).isNotNull().isNotEmpty().containsExactly(0, 1, 2, 3);
    }

    @Test
    @DisplayName("literal expression works correctly")
    void literalExpressionWorksCorrectlyTest() {
        // given, when
        val bytesWithLombok = "'hello world'.concat('!').bytes".execute(byte[].class);
        val bytesWithoutLombok = SpelUtils.execute("'hello world'.concat('!').bytes", byte[].class);

        // then
        assertThat(new String(bytesWithLombok)).isNotNull().isEqualTo("hello world!");
        assertThat(new String(bytesWithoutLombok)).isNotNull().isEqualTo("hello world!");
    }
}
