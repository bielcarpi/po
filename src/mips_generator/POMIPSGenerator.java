package mips_generator;

import entities.*;
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

            // TODO Add the .data section

            //Add the .text section
            out.println(".text");

            // If we have a global block, add a 'j $global' and a 'j $main' at the end of the global block
            // else, add a 'j $main'
            if(entries.containsKey("global")) {
                out.println("j $global");
                entries.get("global").get(0).getEntries().add(new TACEntry("j", "main", null, TACType.GOTO));
            }
            else{
                out.println("j $main");
            }

            // For each hashmap entry
            for (String funcName : entries.keySet()) {
                if(funcName.equals("main")) continue; // Skip the main func (it needs to be the last func)

                // For each block in the function
                out.println("\n$" + funcName + ":");
                for (TACBlock block : entries.get(funcName)) {
                    if(block.getBlockNum() != -1){ //If the block has a label, print it
                        out.println("$E" + block.getBlockNum() + ":");
                    }
                    // For each entry in the block
                    for(TACEntry tacEntry : block.getEntries()){
                        String asm = MIPSConverter.convert(tacEntry);
                        if(asm != null) out.println(asm);
                    }
                }
            }

            // Print the main function
            out.println("\n$main:");
            for (TACBlock block : entries.get("main")) {
                if(block.getBlockNum() != -1){ //If the block has a label, print it
                    out.println("$E" + block.getBlockNum() + ":");
                }
                // For each entry in the block
                for(TACEntry tacEntry : block.getEntries()){
                    String asm = MIPSConverter.convert(tacEntry);
                    if(asm != null) out.println(asm);
                }
            }

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
    }
}















