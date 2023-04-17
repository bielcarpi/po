package tests;

import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class POPreprocessorTests {
    private final POPreprocessor poPreprocessor = new POPreprocessor();
    @Test
    void funcMainEmpty() {
        String actual = this.poPreprocessor.generatePureHighLevelLanguage("src/tests/unit/po_preprocessor/inputs/func-main-empty.po");
        try {
            String expected = Files.readString(Path.of("src/tests/unit/po_preprocessor/outputs/func-main-empty.po"));
            //expected = expected.replaceAll("\\r\\n", "\n");
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void funcMainInlineComment() {
        String actual = this.poPreprocessor.generatePureHighLevelLanguage("src/tests/unit/po_preprocessor/inputs/func-main-inline-comment.po");
        try {
            String expected = Files.readString(Path.of("src/tests/unit/po_preprocessor/outputs/func-main-inline-comment.po"));
            //expected = content.replaceAll("\\r\\n", "\n");
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void funcMainInlineComments() {
        String actual = this.poPreprocessor.generatePureHighLevelLanguage("src/tests/unit/po_preprocessor/inputs/func-main-inline-comments.po");
        try {
            String expected = Files.readString(Path.of("src/tests/unit/po_preprocessor/outputs/func-main-inline-comments.po"));
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}