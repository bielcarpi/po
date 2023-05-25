package mips_generator;

import entities.*;
import org.jetbrains.annotations.NotNull;

public class MIPSConverter {

    private static final String WORK_REG_1 = "$s0"; // Working register used for arg1
    private static final String WORK_REG_2 = "$s1"; // Working register used for arg1
    private static final String WORK_REG_3  = "$s2"; // Working register used for result
    private static final String RETURN_REG = "$v0"; // Register used for return values

    private static final String RAM_REG_1 = "t9"; // Register used for arg1 in RAM

    private static final String RAM_REG_2 = "t8"; // Register used for arg2 in RAM
    private static final String RETURN_ADDR_REG = "$ra"; // Register used for return from function
    private static final String[] ARG_REGS = {"$a0", "$a1", "$a2", "$a3"}; // Registers used for arguments

    private static int GLOBAL_REG_COUNT = 0; // Number of registers used for global variables

    private static int lastStackPointer = 0; // Last stack pointer used



    private static String assignLiteral(String dest, String literal) {
        return "li " + dest + ", " + literal;
    }

    public static String convert(TACEntry tacEntry){
        StringBuilder sb = new StringBuilder();
        String destAux = null;

        //If we have either arg1 or arg2 or dest in RAM, load them into a register
        if(isInRAM(tacEntry.getArg1(), tacEntry.getScope())){
            sb.append("\tla $").append(RAM_REG_1).append(", ").append(tacEntry.getArg1()).append("\n");
            sb.append("\tlw $").append(RAM_REG_1).append(", 0($").append(RAM_REG_1).append(")\n");
            tacEntry.setArg1(RAM_REG_1);
        }
        if(isInRAM(tacEntry.getArg2(), tacEntry.getScope())){
            sb.append("\tla $").append(RAM_REG_2).append(", ").append(tacEntry.getArg2()).append("\n");
            sb.append("\tlw $").append(RAM_REG_2).append(", 0($").append(RAM_REG_2).append(")\n");
            tacEntry.setArg2(RAM_REG_2);
        }
        if(isInRAM(tacEntry.getDest(), tacEntry.getScope())){
            destAux = tacEntry.getDest();
            sb.append("\tla $").append(RAM_REG_1).append(", ").append(tacEntry.getDest()).append("\n");
            sb.append("\tlw $").append(RAM_REG_1).append(", 0($").append(RAM_REG_1).append(")\n");
            tacEntry.setDest(RAM_REG_1);
        }


        switch (tacEntry.getType()) {
            case ADD, SUB, MUL, DIV, AND, OR -> generateOperationMIPS(sb, tacEntry);
            case EQU -> generateAssignmentMIPS(sb, tacEntry);
            case IFEQU, IFNEQ, IFGEQ, IFLEQ, IFG, IFL -> generateConditionalMIPS(sb, tacEntry);
            case GOTO -> generateGotoMIPS(sb, tacEntry);
            case RET -> generateReturnMIPS(sb, tacEntry);
            case CALL -> generateCallMIPS(sb, tacEntry);
            case SYSCALL -> generateSyscallMIPS(sb, tacEntry);
            case ADD_PARAM -> generateAddParamMIPS(sb, tacEntry);
            case SAVE_CONTEXT -> generateSaveContextMIPS(sb);
            case LOAD_CONTEXT -> generateLoadContextMIPS(sb);
        }

        //Load it again to RAM
        if(destAux != null){
            sb.append("\n\tla $").append(RAM_REG_2).append(", ").append(destAux).append("\n");
            sb.append("\tsw $").append(RAM_REG_1).append(", 0($").append(RAM_REG_2).append(")");
        }

        return sb.toString();
    }

    private static void generateLoadContextMIPS(StringBuilder sb) {
        //Restore the context
        //Modify the stack pointer and pop the last stack frame
        int i = lastStackPointer;
        sb.append("\n\n\tsubi $sp, $sp, ").append(i * 4).append("\n");

        //Restore the current arguments
        for(i = 0; i < ARG_REGS.length; i++){
            sb.append("\tlw ").append(ARG_REGS[i]).append(", ").append(i * 4).append("($sp)\n");
        }

        //Restore the return address
        sb.append("\tlw $ra, ").append(ARG_REGS.length * 4).append("($sp)\n");

        //Restore all the $tx registers in use (except the ones that are global)
        int registersToRestore = GLOBAL_REG_COUNT;
        for(i = ARG_REGS.length + 1; registersToRestore <= 9; i++, registersToRestore++){
            sb.append("\tlw $t").append(registersToRestore).append(", ").append(i * 4).append("($sp)\n");
        }

        //Restore the $s registers
        sb.append("\tlw $s0").append(", ").append(i++ * 4).append("($sp)\n");
        sb.append("\tlw $s1").append(", ").append(i++ * 4).append("($sp)\n");
        sb.append("\tlw $s2").append(", ").append(i * 4).append("($sp)\n");
    }

    private static void generateSaveContextMIPS(StringBuilder sb) {
        sb.append("\n");

        //Save the current arguments
        for(int i = 0; i < ARG_REGS.length; i++){
            sb.append("\tsw ").append(ARG_REGS[i]).append(", ").append(i * 4).append("($sp)\n");
        }

        //Save the return address
        sb.append("\tsw $ra, ").append(ARG_REGS.length * 4).append("($sp)\n");

        //Save all the $tx registers in use (except the ones that are global)
        int registersToSave = GLOBAL_REG_COUNT;
        int i;
        for(i = ARG_REGS.length + 1; registersToSave <= 9; i++, registersToSave++){
            sb.append("\tsw $t").append(registersToSave).append(", ").append(i * 4).append("($sp)\n");
        }

        //Save the $s registers in use
        sb.append("\tsw $s0").append(", ").append(i++ * 4).append("($sp)\n");
        sb.append("\tsw $s1").append(", ").append(i++ * 4).append("($sp)\n");
        sb.append("\tsw $s2").append(", ").append(i++ * 4).append("($sp)\n");

        //Modify the stack pointer to make room for the new stack frame
        sb.append("\taddi $sp, $sp, ").append(i * 4).append("\n\n");

        lastStackPointer = i;
    }

    private static void generateSyscallMIPS(StringBuilder sb, TACEntry tacEntry) {
        sb.append("\tli $v0, ").append(tacEntry.getBlockNum()).append("\n\tsyscall");
    }


    private static void generateAddParamMIPS(StringBuilder sb, TACEntry tacEntry){
        if(isLiteral(tacEntry.getArg2())){
            //If arg1 is a literal
            sb.append("\t").append(assignLiteral(ARG_REGS[tacEntry.getBlockNum()], tacEntry.getArg2()));
        }
        else if(tacEntry.getArg2().startsWith(SymbolTable.INTERNAL_PREFIX)){
            //If arg1 is a string
            sb.append("\tla ").append(ARG_REGS[tacEntry.getBlockNum()]).append(", ")
                    .append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb));
        }
        else{
            //If arg1 is a normal variable
            sb.append("\tmove ").append(ARG_REGS[tacEntry.getBlockNum()]).append(", ")
                    .append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb));
        }
    }

    private static void generateCallMIPS(StringBuilder sb, TACEntry tacEntry) {
        //Do the actual call
        sb.append("\tjal $").append(Syscall.isSyscall(tacEntry.getArg1()) ? Syscall.get(tacEntry.getArg1()) : tacEntry.getArg1());
    }


    private static void generateReturnMIPS(StringBuilder sb, TACEntry tacEntry) {

        //If we're in main, syscall 10 (exit)
        if(tacEntry.getScope().equals(SymbolTable.MAIN_SCOPE)){
            sb.append("\tli $v0, 10\n\tsyscall");
            return;
        }

        if (isLiteral(tacEntry.getArg1())) {
            //If arg1 is a literal
            sb.append("\t").append(assignLiteral(RETURN_REG, tacEntry.getArg1()));
        }
        else {
            //If arg1 is a variable
            sb.append("\tmove ").append(RETURN_REG).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb));
        }

        sb.append("\n\tjr ").append(RETURN_ADDR_REG);
    }

    private static void generateGotoMIPS(StringBuilder sb, TACEntry tacEntry) {
        if(tacEntry.getDest() != null){
            //If the goto is a label
            sb.append("\tj $").append(tacEntry.getDest());
        }
        else{
            //If the goto is a block number
            sb.append("\tj $E").append(tacEntry.getBlockNum());
        }
    }

    private static void generateConditionalMIPS(StringBuilder sb, TACEntry tacEntry) {
        String conditionalType = switch (tacEntry.getType()) {
            case IFEQU -> "beq ";
            case IFNEQ -> "bne ";
            case IFGEQ -> "bge ";
            case IFLEQ -> "ble ";
            case IFG -> "bgt ";
            case IFL -> "blt ";
            default -> null;
        };

        if(isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())){
            //If both args are literals
            sb.append("\t").append(conditionalType).append(tacEntry.getArg1()).append(", ").append(tacEntry.getArg2())
                    .append(", $E").append(tacEntry.getBlockNum());
        }
        else if(isLiteral(tacEntry.getArg1()) && !isLiteral(tacEntry.getArg2())){
            //If arg2 is a variable and arg1 is a literal
            sb.append("\t").append(conditionalType).append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb))
                    .append(", ").append(tacEntry.getArg1()).append(", $E").append(tacEntry.getBlockNum());
        }
        else if(!isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())){
            //If arg1 is a variable and arg2 is a literal
            sb.append("\t").append(conditionalType).append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb))
                    .append(", ").append(tacEntry.getArg2()).append(", $E").append(tacEntry.getBlockNum());
        }
        else{ //If both are variables
            sb.append("\t").append(conditionalType).append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb))
                    .append(", ").append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb))
                    .append(", $E").append(tacEntry.getBlockNum());
        }
    }

    private static void generateAssignmentMIPS(StringBuilder sb, TACEntry tacEntry) {
        if(isLiteral(tacEntry.getArg1())){
            //If arg1 is a literal
            sb.append("\t").append(assignLiteral(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb), tacEntry.getArg1()));
        }
        else if(tacEntry.getArg1().startsWith(SymbolTable.INTERNAL_PREFIX)) {
            //If arg1 is a string
            sb.append("\tla ").append(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb)).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb));
        }
        else{
            //If arg1 is a variable
            sb.append("\tmove ").append(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb)).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb));
        }
    }

    private static boolean isLiteral(String toValidate) {
        if(toValidate.startsWith("0x")) return true; //Check if string is a hex number
        return toValidate.matches("^[0-9]+"); //Check if string is a number
    }

    private static void generateOperationMIPS(StringBuilder sb, TACEntry tacEntry){
        sb.append("\t");

        String operationType = switch (tacEntry.getType()) {
            case ADD -> "add ";
            case SUB -> "sub ";
            case MUL -> "mul ";
            case DIV -> "div ";
            case AND -> "and ";
            case OR -> "or ";
            default -> null;
        };

        if(isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())) {
            // 1. If both are constants
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg1())).append("\n\t");
            sb.append(assignLiteral(WORK_REG_3, tacEntry.getArg2())).append("\n\t");
            sb.append(operationType).append(WORK_REG_1).append(", ").append(WORK_REG_2).append(", ").append(WORK_REG_3).append("\n\t");

        } else if(isLiteral(tacEntry.getArg1()) && !isLiteral(tacEntry.getArg2())){
            // 2. If only arg1 is constant
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg2())).append("\n\t");
            sb.append(operationType).append(WORK_REG_1).append(", ").append(WORK_REG_2).append(", ").append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb)).append("\n\t");

        } else if(!isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())){
            // 3. If only arg2 is constant
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg2())).append("\n\t");
            sb.append(operationType).append(WORK_REG_1).append(", ").append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb)).append(", ").append(WORK_REG_2).append("\n\t");
        } else {
            // 4. If both are variables
            sb.append(operationType).append(WORK_REG_1).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb)).append(", ").append(getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb)).append("\n\t");
        }

        // Lastly assign WORK_REG_1 value to the destination
        if(SymbolTable.getInstance().lookup(tacEntry.getDest(), tacEntry.getScope()) != null)
            sb.append("move ").append(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb)).append(", ").append(WORK_REG_1);
        else if(!WORK_REG_1.contains(tacEntry.getDest())) //Only assign if the destination is not the same as the WORK_REG_1
            sb.append("move ").append(tacEntry.getDest()).append(", ").append(WORK_REG_1);
        else
            sb.delete(sb.length() - 2, sb.length()); //Remove the last \n\t
    }

    private static String getVariableRegister(@NotNull String name, @NotNull String scope, @NotNull StringBuilder sb){
        if(name.startsWith("0x")) return name; //If it is a memory address, return it

        if(SymbolTable.getInstance().lookup(name, scope) == null)
            return "$" + name;

        SymbolTableVariableEntry entry = ((SymbolTableVariableEntry)SymbolTable.getInstance().lookup(name, scope));
        if(entry == null) return "$" + name;

        int registerID = entry.getRegisterID();

        //If the variable is in a register, return it (either $a if it is a parameter or $t)
        if(registerID != -1)
            return entry.isParameter() ? "$a" + registerID : "$t" + registerID;

        //Else, load it from memory into a register
        if(entry.getStringValue() != null){ //If we have a string, load its address
            return entry.getId();
        }

        return null;
    }

    /**
     * Sets the global register count
     * @param count The global register count
     */
    public static void setGlobalRegCount(int count) {
        GLOBAL_REG_COUNT = count;
    }

    private static boolean isInRAM(String name, String scope){
        if(name == null || isLiteral(name)) return false;
        if(scope == null) scope = SymbolTable.GLOBAL_SCOPE;

        SymbolTableEntry ste = SymbolTable.getInstance().lookup(name, scope);
        if (ste == null) return false;
        if (ste.entryType() == TokenType.FUNC) return false;
        int entryRegisterID = ste.getRegisterID();
        return entryRegisterID == -1;
    }
}
