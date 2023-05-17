package intermediate_code_optimizer;

import entities.TAC;
import org.jetbrains.annotations.NotNull;

public class POTACOptimizer implements TACOptimizer{

    @Override
    public @NotNull TAC optimizeTAC(@NotNull TAC tac) {
        return tac;
    }
}
