package mips_generator;

import entities.TAC;
import org.jetbrains.annotations.NotNull;

public interface MIPSGenerator {

    /**
     * Generates the MIPS code from the given TAC
     * @param tac the TAC
     */
    void generateMIPS(@NotNull TAC tac);
}
