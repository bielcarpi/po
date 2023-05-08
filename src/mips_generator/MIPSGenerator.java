package mips_generator;

import org.jetbrains.annotations.NotNull;

public interface MIPSGenerator {

    /**
     * Generates the MIPS code from the given TAC
     * @param tac the TAC
     * @param fileName the name of the file to be generated, in which the MIPS code will be written
     */
    void generateMIPS(@NotNull String tac, @NotNull String fileName);
}
