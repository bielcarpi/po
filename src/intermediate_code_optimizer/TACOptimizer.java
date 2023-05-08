package intermediate_code_optimizer;

import org.jetbrains.annotations.NotNull;

public interface TACOptimizer {

    /**
     * Optimizes the TAC
     * @param tac the TAC to be optimized
     * @return the optimized TAC
     */
    @NotNull String optimizeTAC(@NotNull String tac);
}
