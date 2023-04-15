package tests;

import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class POPreprocessorTests {
    @Test
    void simpleFuncMainInlineComment() {
        POPreprocessor poPreprocessor = new POPreprocessor();
        String result = poPreprocessor.generatePureHighLevelLanguage("src/tests/unit/po_preprocessor/inputs/func-main-inline-comment.po");
        try {
            String content = Files.readString(Path.of("src/tests/unit/po_preprocessor/outputs/func-main-inline-comment.po"));
            content = content.replaceAll("\\r\\n", "\n");
            assertEquals(content, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void simpleFuncMainInlineComments() {
        POPreprocessor poPreprocessor = new POPreprocessor();
        String result = poPreprocessor.generatePureHighLevelLanguage("src/tests/unit/po_preprocessor/inputs/func-main-inline-comments.po");
        try {
            String content = Files.readString(Path.of("src/tests/unit/po_preprocessor/outputs/func-main-inline-comments.po"));
            assertEquals(content, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}