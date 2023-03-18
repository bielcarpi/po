package syntax_analysis;

import entities.AbstractSyntaxTree;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import semantic_analysis.SemanticAnalyzer;

public interface Parser {

    AbstractSyntaxTree generateAbstractSyntaxTree(@NotNull Lexer lexer, @NotNull SemanticAnalyzer semanticAnalyzer);

}
