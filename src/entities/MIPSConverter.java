package entities;

public class MIPSConverter {

    private static final String WORK_REG_1 = "$t9"; // Working register used for arg1
    private static final String WORK_REG_2 = "$t8"; // Working register used for arg1
    private static final String WORK_REG_3  = "$t7"; // Working register used for result

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

            default -> {
                return null;
            }
        }
    }

    private static String generateReturnMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        // TODO: Check recursive calls and return literals/variables
        /*if (isLiteral(tacEntry.getArg1())) {
            sb.append(assignLiteral(WORK_REG_1, tacEntry.getArg1())).append("\n");
            sb.append("move $v0, " + WORK_REG_1).append("\n");
        } else {
            sb.append("move $v0, " + getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb)).append("\n");
        }*/

        return null;
    }

    private static String generateGotoMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        sb.append("j $E").append(tacEntry.getBlockNum()).append("\n");
        return sb.toString();
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

        return null;
    }

    private static String generateAssignmentMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        sb.append(assignLiteral(WORK_REG_1, getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb))).append("\n");;
        sb.append("move " + tacEntry.getDest() + ", " + WORK_REG_1);
        return sb.toString();
    }

    private static boolean isLiteral(String toValidate) {
        return toValidate.matches("[0-9]+"); //Check if string is a number
    }

    private static String generateOperationMIPS(TACEntry tacEntry){
        StringBuilder sb = new StringBuilder();

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
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg1())).append("\n");
            sb.append(assignLiteral(WORK_REG_3, tacEntry.getArg2())).append("\n");
            sb.append(operationType  + WORK_REG_1 + ", " + WORK_REG_2 + ", " + WORK_REG_3).append("\n");

        } else if(isLiteral(tacEntry.getArg1()) && !isLiteral(tacEntry.getArg2())){
            // 2. If only arg1 is constant
            sb.append(assignLiteral(WORK_REG_2, tacEntry.getArg1())).append("\n");
            sb.append(operationType + WORK_REG_1 + ", " + WORK_REG_2 + ", " + tacEntry.getArg2()).append("\n");

        }else if(!isLiteral(tacEntry.getArg1()) && isLiteral(tacEntry.getArg2())){
            // 3. If only arg2 is constant
            sb.append(assignLiteral(WORK_REG_1, tacEntry.getArg2())).append("\n");
            sb.append(operationType + WORK_REG_1 + ", " + WORK_REG_2 + ", " + getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb)).append("\n");
        }else {
            // 4. If both are variables
            sb.append(operationType + WORK_REG_1 + ", " + getVariableRegister(tacEntry.getArg1(), tacEntry.getScope(), sb) + ", " +
                    getVariableRegister(tacEntry.getArg2(), tacEntry.getScope(), sb)).append("\n");
        }

        // Lastly assign WORK_REG_3 value to the destination
        if(SymbolTable.getInstance().lookup(tacEntry.getDest(), tacEntry.getScope()) != null)
            sb.append("move " + WORK_REG_1 + ", " + getVariableRegister(tacEntry.getDest(), tacEntry.getScope(), sb));
        else
            sb.append("move " + WORK_REG_1 + ", " + tacEntry.getDest());

        return sb.toString();
    }

}
