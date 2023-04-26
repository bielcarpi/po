package testing;

import lexical_analysis.POLexer;
import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;
import semantic_analysis.POSemanticAnalyzer;
import syntax_analysis.POParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class POParseTreeTests {

    private void test(String input, String output) {
        try {
            String pureHLL = new POPreprocessor().generatePureHighLevelLanguage(input);
            String actual = Objects.requireNonNull(new POParser(pureHLL).generateParseTree(new POLexer(), new POSemanticAnalyzer())).toString();
            String expected = Files.readString(Path.of(output));
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void longSum() {
        test("src/tests/unit/po_parsetree/inputs/long-sum.po", "src/tests/unit/po_parsetree/outputs/long-sum.po");
    }

    @Test
    void loopExpressionsFunctions() {
        test("src/tests/unit/po_parsetree/inputs/loop-expressions-functions.po", "src/tests/unit/po_parsetree/outputs/loop-expressions-functions.po");
    }

    @Test
    void complexExpressions() {
        test("src/tests/unit/po_parsetree/inputs/complex-expressions.po", "src/tests/unit/po_parsetree/outputs/complex-expressions.po");
    }

    @Test
    void conditionalsIf() {
        test("src/tests/unit/po_parsetree/inputs/conditionals-if.po", "src/tests/unit/po_parsetree/outputs/conditionals-if.po");
    }

    @Test
    void conditionalsSwitch() {
        test("src/tests/unit/po_parsetree/inputs/conditionals-switch.po", "src/tests/unit/po_parsetree/outputs/conditionals-switch.po");
    }
}