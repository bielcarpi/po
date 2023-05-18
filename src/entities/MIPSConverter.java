package entities;

import org.jetbrains.annotations.NotNull;

public class MIPSConverter {

    private static final String WORK_REG_1 = "$t9"; // Working register used for arg1
    private static final String WORK_REG_2 = "$t8"; // Working register used for arg1
    private static final String WORK_REG_3  = "$t7"; // Working register used for result
    private static final String RETURN_REG = "$v0"; // Register used for return values
    private static final String[] ARG_REGS = {"$a0", "$a1", "$a2", "$a3"}; // Registers used for arguments

    private static String assignLiteral(String dest, String literal) {
        return "li " + dest + ", " + literal;
    }

    public static String convert(TACEntry tacEntry){
        switch (tacEntry.getType()) {
            case ADD, SUB, MUL, DIV, AND, OR -> {
                return generateOperationMIPS(tacEntry);
            }
            case EQU -> {
                return generateAssignmentMIPS(tacEntry);
            }
            case IFEQU, IFNEQ, IFGEQ, IFLEQ, IFG, IFL -> {
                return generateConditionalMIPS(tacEntry);
            }
            case GOTO -> {
                return generateGotoMIPS(tacEntry);
            }
            case RET -> {
                return generateReturnMIPS(tacEntry);
            }
            case CALL -> {
                return generateCallMIPS(tacEntry);
            }

            default -> {
                return null;
            }
        }
    }

    private static String generateCallMIPS(TACEntry tacEntry) {
        return "\tjal " + tacEntry.getArg1();
    }

    private static String generateReturnMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        if (isLiteral(tacEntry.getArg1())) {
            //If arg1 is a literal
            sb.append("\t").append(assignLiteral(RETURN_REG, tacEntry.getArg1()));
        } else {
            //If arg1 is a variable
            sb.append("\tmove ").append(RETURN_REG).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb));
        }

        return sb.toString();
    }

    private static String generateGotoMIPS(TACEntry tacEntry) {
        if(tacEntry.getDest() != null){
            //If the goto is a label
            return "\tj $" + tacEntry.getDest();
        }
        else{
            //If the goto is a block number
            return "\tj $E" + tacEntry.getBlockNum();
        }
    }

    private static String generateConditionalMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
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

        return sb.toString();
    }

    private static String generateAssignmentMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        if(isLiteral(tacEntry.getArg1())){
            //If arg1 is a literal
            sb.append("\t").append(assignLiteral(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb), tacEntry.getArg1()));
        }
        else{
            //If arg1 is a variable
            sb.append("\tmove ").append(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb)).append(", ")
                    .append(getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb));
        }

        return sb.toString();
    }

    private static boolean isLiteral(String toValidate) {
        return toValidate.matches("[0-9]+"); //Check if string is a number
    }

    private static String generateOperationMIPS(TACEntry tacEntry){
        StringBuilder sb = new StringBuilder();
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
            sb.append(operationType  + WORK_REG_1 + ", " + WORK_REG_2 + ", " + WORK_REG_3).append("\n\t");

        } else if(isLiteral(tacEntry.getArg1()) && !isLiteral(tacEntry.getArg2())){
            // 2. If only arg1 is constant
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg2())).append("\n\t");
            sb.append(operationType + WORK_REG_1 + ", " + WORK_REG_2 + ", " + getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb)).append("\n\t");

        } else if(!isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())){
            // 3. If only arg2 is constant
            sb.append(assignLiteral(WORK_REG_1, tacEntry.getArg2())).append("\n\t");
            sb.append(operationType + WORK_REG_1 + ", " + WORK_REG_2 + ", " + getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb)).append("\n\t");
        } else {
            // 4. If both are variables
            sb.append(operationType + WORK_REG_1 + ", " + getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb) + ", " +
                    getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb)).append("\n\t");
        }

        // Lastly assign WORK_REG_3 value to the destination
        if(SymbolTable.getInstance().lookup(tacEntry.getDest(), tacEntry.getScope()) != null)
            sb.append("move " + WORK_REG_1 + ", ").append(getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb));
        else if(!WORK_REG_1.contains(tacEntry.getDest())) //Only assign if the destination is not the same as the WORK_REG_1
            sb.append("move " + WORK_REG_1 + ", ").append(tacEntry.getDest());
        else
            sb.delete(sb.length() - 2, sb.length()); //Remove the last \n\t

        return sb.toString();
    }

    private static String getVariableRegister(@NotNull String name, @NotNull String scope, @NotNull StringBuilder sb){
        if(SymbolTable.getInstance().lookup(name, scope) == null)
            return "$" + name;

        int programID = ((SymbolTableVariableEntry)SymbolTable.getInstance().lookup(name, scope)).getProgramID();
        //TODO if programID == -1, the variable doesn't have a register. Load it from RAM
        return "$t" + programID;
    }
}
