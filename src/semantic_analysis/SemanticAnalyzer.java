package semantic_analysis;

import entities.AbstractSyntaxTree;
import org.jetbrains.annotations.NotNull;

public interface SemanticAnalyzer {

    void validateAbstractSyntaxTree(@NotNull AbstractSyntaxTree ast);
}
