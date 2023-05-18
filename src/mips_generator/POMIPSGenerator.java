package mips_generator;

import entities.MIPSConverter;
import entities.TAC;
import entities.TACBlock;
import entities.TACEntry;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class POMIPSGenerator implements MIPSGenerator {

    private final String fileName;
    private HashMap<String, ArrayList<TACBlock>> entries;

    public POMIPSGenerator(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void generateMIPS(@NotNull TAC tac) {
        //Start printing the asm file
        try (PrintWriter out = new PrintWriter(fileName)) {
            entries = tac.getEntries(); // Get the map of function names to TACBlocks

            // For each hashmap entry
            for (String funcName : entries.keySet()) {
                // For each block in the function
                out.println("\n$" + funcName + ":");
                for (TACBlock block : entries.get(funcName)) {
                    if(block.getBlockNum() != -1){
                        out.println("\n$E" + block.getBlockNum() + ":");
                    }
                    // For each entry in the block
                    for(TACEntry tacEntry : block.getEntries()){
                        out.println(MIPSConverter.convert(tacEntry));
                    }
                }
            }

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
    }
}















