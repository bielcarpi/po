package entities;

public enum TACType {
    ADD,
    SUB,
    MUL,
    DIV,
    AND,
    OR,
    EQU,
    IFEQU, //IF EQual
    IFGEQ, //IF Greater or EQual
    IFLEQ, //IF Less or EQual
    IFG, //IF Greater
    IFL, //IF Less
    GOTO,
    RET;

    public static TACType getType(TokenType op) {
        return switch (op) {
            case ADD, DOUBLE_ADD -> ADD;
            case SUB, DOUBLE_SUB -> SUB;
            case MULT -> MUL;
            case DIV -> DIV;
            case AND -> AND;
            case OR -> OR;
            case EQU -> EQU;
            default -> null;
        };
    }

    public String toString(){
        return switch (this) {
            case ADD -> "+";
            case SUB -> "-";
            case MUL -> "*";
            case DIV -> "/";
            case AND -> "and";
            case OR -> "or";
            case EQU -> "=";
            case IFEQU -> "==";
            case IFGEQ -> ">=";
            case IFLEQ -> "<=";
            case IFG -> ">";
            case IFL -> "<";
            case GOTO -> "goto";
            case RET -> "ret";
        };
    }
}
