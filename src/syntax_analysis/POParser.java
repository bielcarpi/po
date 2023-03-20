package syntax_analysis;

import entities.ParseTree;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import semantic_analysis.SemanticAnalyzer;

public class POParser implements Parser {

    final String pureHLL;

    public POParser(@NotNull final String pureHLL) {
        this.pureHLL = pureHLL;
    }

    @Override
    public @NotNull ParseTree generateParseTree(@NotNull Lexer lexer, @NotNull SemanticAnalyzer semanticAnalyzer) {
        return null;
    }
}
