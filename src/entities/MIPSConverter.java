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

            default -> {
                return null;
            }
        }
    }

    private static String generateBitwiseMIPS(TACEntry tacEntry) {
        return null;
    }

    private static String generateAssignmentMIPS(TACEntry tacEntry) {
        StringBuilder sb = new StringBuilder();
        sb.append(assignLiteral(WORK_REG_1, tacEntry.getArg1())).append("\n");;
        sb.append("sw " + tacEntry.getDest() + ", " + WORK_REG_1).append("\n");;
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
            sb.append(operationType + WORK_REG_1 + ", " + WORK_REG_2 + ", " + tacEntry.getArg1()).append("\n");
        }

        // Lastly assign WORK_REG_3 value to the destination
        sb.append("sw " + WORK_REG_1 + ", " + tacEntry.getDest()).append("\n");

        return sb.toString();
    }

}
