package tests;

import entities.TokenStream;
import lexical_analysis.POLexer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class POLexerTest {
    @Test
    void simpleEmptyFuncMain() {
        POLexer poLexer = new POLexer();
        try {
            TokenStream tokenStream = poLexer.generateTokenStream(Files.readString(Path.of("src/tests/unit/po_lexer/inputs/simple-func-main.po")));
            String expected =  Files.readString(Path.of("src/tests/unit/po_lexer/outputs/simple-func-main.po"));
            expected = expected.replaceAll("\\r\\n", "\n");
            if (tokenStream != null) {
                assertEquals(expected, tokenStream.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}