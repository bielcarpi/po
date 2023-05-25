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
        //Assign registers to the most used variables
        assignRegisters();

        //Start printing the asm file
        try (PrintWriter out = new PrintWriter(fileName)) {
            entries = tac.getEntries(); // Get the map of function names to TACBlocks

            //Check if we have strings to add to the .data section
            ArrayList<SymbolTableVariableEntry> vars = SymbolTable.getInstance().getVariableEntries();
            if(vars != null && vars.size() > 0){
                StringBuilder sb = new StringBuilder();
                sb.append(".data\n");
                boolean written = false;
                for(SymbolTableVariableEntry entry : vars){
                    if(entry.getType() == TokenType.STRING){
                        sb.append("\t").append(entry.getId()).
                                append(": .asciiz ").
                                append(entry.getStringValue());
                        written = true;
                    }
                }

                if(written) out.println(sb);
            }



            //Add the .text section
            out.println(".text");

            // If we have a global block, add a 'j $global' and a 'j $main' at the end of the global block
            // else, add a 'j $main'
            if(entries.containsKey(SymbolTable.GLOBAL_SCOPE)) {
                out.println("j $" + SymbolTable.GLOBAL_SCOPE);
                entries.get(SymbolTable.GLOBAL_SCOPE).get(0).getEntries().add(new TACEntry("j", SymbolTable.MAIN_SCOPE, null, TACType.GOTO));
            }
            else{
                out.println("j $" + SymbolTable.MAIN_SCOPE);
            }

            // For each hashmap entry
            for (String funcName : entries.keySet()) {
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
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }

        System.out.println(SymbolTable.getInstance().toString());;
    }


    /**
     * Assigns registers to the most used variables (the ones that are used the most times).
     *
     * We have 10 total registers for temporary variables ($t0-$t9). We will use these to store the most used variables.
     * With this we will reduce the number of loads and stores to main memory, and therefore improve performance.
     * We need to keep in mind that we may need one of these registers to store a variable in RAM (in order to perform
     *  an operation with it, for example). The max num of variables in RAM that we may need to load is 2 (one for each
     *  operand of an operation). Therefore, we will assign the 8 most used variables to the registers, and the other 2
     *  will be used to temporarily store the variables in RAM.
     *
     *  We need to keep in mind that, as we save context in the stack, the 8 most used variables can be different in
     *  different scopes. Therefore, we will assign registers to the most used variables in each scope.
     *  The only exception is when we assign registers to the global scope. In this case, in function scope we won't have
     *  8 registers available, but 8-GLOBAL_VARIABLES_IN_REGISTERS. We'll keep track of this value in {@code availableRegisters}.
     *
     */
    private void assignRegisters(){
        //Check most used variables in the program
        ArrayList<SymbolTableVariableEntry> entries = SymbolTable.getInstance().getVariableEntries();
        if(entries == null || entries.isEmpty()) return;

        //Sort the entries by num of uses
        entries.sort((o1, o2) -> o2.getNumTimesUsed() - o1.getNumTimesUsed());

        //Check which ones of the 8 most used variables are in the global scope
        int globalVariablesInRegisters = 0;
        for(int i = 0; i < 8 && i < entries.size(); i++){
            if(entries.get(i).getScope().equals(SymbolTable.GLOBAL_SCOPE) &&
                    entries.get(i).getType() != TokenType.STRING){
                entries.get(i).setRegisterID(globalVariablesInRegisters);
                globalVariablesInRegisters++;
            }
        }
        MIPSConverter.setGlobalRegCount(globalVariablesInRegisters);

        //We now have 8-GLOBAL_VARIABLES_IN_REGISTERS registers available for the function scope
        int availableRegisters = 8 - globalVariablesInRegisters;
        if(availableRegisters <= 0) return; //If we don't have any registers available, we're done

        //Get all scopes
        ArrayList<String> scopes = new ArrayList<>(SymbolTable.getInstance().getScopes());
        scopes.remove(SymbolTable.GLOBAL_SCOPE);

        //For each scope, assign registers to the 8-GLOBAL_VARIABLES_IN_REGISTERS most used variables in the scope
        for(String scope : scopes){
            int scopeAssignedRegisters = 0;
            //Get the variables in the scope
            ArrayList<SymbolTableVariableEntry> scopeEntries = SymbolTable.getInstance().getVariableEntries(scope);
            if(scopeEntries == null || scopeEntries.isEmpty()) continue;

            //Sort the entries by num of uses
            scopeEntries.sort((o1, o2) -> o2.getNumTimesUsed() - o1.getNumTimesUsed());

            //Assign registers to the 8-GLOBAL_VARIABLES_IN_REGISTERS most used variables in the scope
            for(SymbolTableVariableEntry entry : scopeEntries){
                if(entry.isParameter() || entry.getStringValue() != null) continue; //Parameters are already assigned a register
                if(scopeAssignedRegisters == availableRegisters) break;
                entry.setRegisterID(globalVariablesInRegisters + scopeAssignedRegisters);
                scopeAssignedRegisters++;
            }
        }
    }
}















