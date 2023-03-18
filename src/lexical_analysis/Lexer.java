package lexical_analysis;

import entities.TokenStream;
import org.jetbrains.annotations.NotNull;

public interface Lexer {

    TokenStream generateTokenStream(@NotNull String program);

}
