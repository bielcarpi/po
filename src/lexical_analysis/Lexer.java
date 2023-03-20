package lexical_analysis;

import entities.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Lexer interface represents a compiler lexer.
 * Its function will be to generate a {@link TokenStream}, given a String which represents
 *  the pure high level language code of the program to compile.
 *
 * @see TokenStream
 */
public interface Lexer {

    /**
     * Given a String that represents the pure high level language code of the program to compile,
     *  generate a {@link TokenStream}.
     * @param program The pure high level language code in String format
     * @return The TokenStream of the HLL code provided
     */
    @Nullable
    TokenStream generateTokenStream(@NotNull String program);

}
