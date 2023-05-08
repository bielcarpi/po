package intermediate_code_generator;

import entities.ParseTree;
import intermediate_code_optimizer.TACOptimizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class POTACGenerator implements TACGenerator{

    private final TACOptimizer tacOptimizer;
    private final boolean outputFile;

    /**
     * Default constructor
     * @param tacOptimizer the optimizer to be used
     * @param outputFile if true, the TAC generated in {@link #generateTAC(ParseTree)} will be written to a file called "output.tac"
     */
    public POTACGenerator(@Nullable TACOptimizer tacOptimizer, boolean outputFile){
        this.tacOptimizer = tacOptimizer;
        this.outputFile = outputFile;
    }


    @Override
    public @NotNull String generateTAC(@NotNull ParseTree pt) {
        return "";
    }
}
