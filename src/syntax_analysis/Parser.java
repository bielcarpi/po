package syntax_analysis;

import entities.ParseTree;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import semantic_analysis.SemanticAnalyzer;

/**
 * The Parser interface represents a parser for a compiler.
 *
 * Its function will be to coordinate the {@link Lexer} and {@link SemanticAnalyzer}, and perform
 *  the syntax analysis in the middle.
 *
 * @see Lexer
 * @see SemanticAnalyzer
 * @see ParseTree
 */
public interface Parser {

    /**
     * Generates a ParseTree for a program, given the {@link Lexer} and {@link SemanticAnalyzer}
     * @param lexer The lexer to be used
     * @param semanticAnalyzer The semantic analyzer to be used
     * @return The ParseTree semantically validated
     */
    @NotNull
    ParseTree generateParseTree(@NotNull Lexer lexer, @NotNull SemanticAnalyzer semanticAnalyzer);

}
