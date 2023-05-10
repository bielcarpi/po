package intermediate_code_generator;

import entities.ParseTree;
import org.jetbrains.annotations.NotNull;

public interface TACGenerator {

    /**
     * Generates the TAC from the given parse tree
     * @param pt the parse tree
     * @return the TAC generated
     */
    @NotNull String generateTAC(@NotNull ParseTree pt);
}
