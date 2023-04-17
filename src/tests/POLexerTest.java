package tests;

import entities.TokenStream;
import lexical_analysis.POLexer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class POLexerTest {
    private final POLexer poLexer = new POLexer();

    @Test
    void simpleEmptyFuncMain() {
        try {
            TokenStream tokenStream = this.poLexer.generateTokenStream(Files.readString(Path.of("src/tests/unit/po_lexer/inputs/simple-empty-func-main.po")));
            String expected =  Files.readString(Path.of("src/tests/unit/po_lexer/outputs/simple-empty-func-main.po")).replaceAll("\\r\\n", "\n");
            if (tokenStream != null) {
                assertEquals(expected, tokenStream.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void simpleFuncMainIf() {
        try {
            TokenStream tokenStream = this.poLexer.generateTokenStream(Files.readString(Path.of("src/tests/unit/po_lexer/inputs/simple-func-main-if.po")));
            String expected =  Files.readString(Path.of("src/tests/unit/po_lexer/outputs/simple-func-main-if.po")).replaceAll("\\r\\n", "\n");
            if (tokenStream != null) {
                assertEquals(expected, tokenStream.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void simpleVariablesAssignation() {
        try {
            TokenStream tokenStream = this.poLexer.generateTokenStream(Files.readString(Path.of("src/tests/unit/po_lexer/inputs/simple-variable-assignation.po")));
            String expected =  Files.readString(Path.of("src/tests/unit/po_lexer/outputs/simple-variable-assignation.po")).replaceAll("\\r\\n", "\n");
            if (tokenStream != null) {
                System.out.println(tokenStream.toString());
                assertEquals(expected, tokenStream.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}