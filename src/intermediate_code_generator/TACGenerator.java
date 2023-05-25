package intermediate_code_generator;

import entities.ParseTree;
import entities.TAC;
import org.jetbrains.annotations.NotNull;

public interface TACGenerator {

    /**
     * Generates the TAC from the given parse tree
     *
     * @param pt the parse tree
     * @return the TAC generated
     */
    @NotNull TAC generateTAC(@NotNull ParseTree pt);
}
