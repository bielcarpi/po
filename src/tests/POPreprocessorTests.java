package tests;

import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class POPreprocessorTests {

    private void test(String input, String output) {
        String actual = new POPreprocessor().generatePureHighLevelLanguage(input);
        try {
            String expected = Files.readString(Path.of(output));
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void funcMainEmpty() {
        test("src/tests/unit/po_preprocessor/inputs/func-main-empty.po", "src/tests/unit/po_preprocessor/outputs/func-main-empty.po");
    }
    @Test
    void funcMainInlineComment() {
        test("src/tests/unit/po_preprocessor/inputs/func-main-inline-comment.po", "src/tests/unit/po_preprocessor/outputs/func-main-inline-comment.po");
    }

    @Test
    void funcMainInlineComments() {
        test("src/tests/unit/po_preprocessor/inputs/func-main-inline-comments.po", "src/tests/unit/po_preprocessor/outputs/func-main-inline-comments.po");
    }
}