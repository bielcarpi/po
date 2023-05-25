package preprocessor;

import org.jetbrains.annotations.NotNull;

/**
 * The Preprocessor interface represents a preprocessor for a compiler toolchain.
 *
 * Its function will be to generate a {@link String}, representing the pure high level language code,
 *  given the path of a code file.
 */
public interface Preprocessor {

    /**
     * Given the path of a code file, return the pure high level language code
     * @param filePath The path of the file that contains the code to read
     * @return The high level language code
     */
    @NotNull
    String generatePureHighLevelLanguage(@NotNull String filePath);
}
