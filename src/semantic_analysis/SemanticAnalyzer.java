package semantic_analysis;

import entities.ParseTree;
import org.jetbrains.annotations.NotNull;

/**
 * The SemanticAnalyzer interface represents a semantic analyzer for a compiler.
 *
 * Its function will be to validate semantically a {@link ParseTree}, and output the
 *  corresponding errors found.
 *
 * @see ParseTree
 */
public interface SemanticAnalyzer {

    /**
     * Validates semantically a {@link ParseTree}, and outputs the corresponding errors.
     * @param ast The ParseTree to validate
     */
    void validateParseTree(@NotNull ParseTree ast);
}
