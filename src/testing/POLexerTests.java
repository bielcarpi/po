package testing;

import entities.TokenStream;
import lexical_analysis.POLexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class POLexerTests {

    private void test(String input, String output) {
        try {
            String pureHLL = new POPreprocessor().generatePureHighLevelLanguage(input);
            TokenStream tokenStream = new POLexer().generateTokenStream(pureHLL);
            String expected =  Files.readString(Path.of(output));
            if (tokenStream != null) {
                Assertions.assertEquals(expected, tokenStream.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void simpleEmptyFuncMain() {
        test("src/testing/unit/po_lexer/inputs/simple-empty-func-main.po", "src/testing/unit/po_lexer/outputs/simple-empty-func-main.po");
    }

    @Test
    void simpleFuncMainIf() {
        test("src/testing/unit/po_lexer/inputs/simple-func-main-if.po", "src/testing/unit/po_lexer/outputs/simple-func-main-if.po");
    }

    @Test
    void simpleVariablesAssignation() {
        test("src/testing/unit/po_lexer/inputs/simple-variable-assignation.po", "src/testing/unit/po_lexer/outputs/simple-variable-assignation.po");
    }



}