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
    IFNEQ, //IF Not EQual
    IFGEQ, //IF Greater or EQual
    IFLEQ, //IF Less or EQual
    IFG, //IF Greater
    IFL, //IF Less
    GOTO,
    RET,
    CALL, //Call function
    SYSCALL, //System call
    ADD_PARAM, //Add parameter
    SAVE_CONTEXT, //Save context
    LOAD_CONTEXT; //Load context

    public static TACType getType(TokenType op) {
        return switch (op) {
            case ADD, DOUBLE_ADD -> ADD;
            case SUB, DOUBLE_SUB -> SUB;
            case MULT -> MUL;
            case DIV -> DIV;
            case AND -> AND;
            case OR -> OR;
            case EQU-> EQU;
            default -> null;
        };
    }

    public static TACType GetAntonym(TokenType type) {
        return switch (type) {
            case DOUBLE_EQU -> IFNEQ;
            case DIFF -> IFEQU;
            case GT -> IFLEQ;
            case LT -> IFGEQ;
            case GTE -> IFL;
            case LTE -> IFG;
            default -> null;
        };
    }

    public static TACType GetEquivalent(TokenType type) {
        return switch (type) {
            case DOUBLE_EQU -> IFEQU;
            case DIFF -> IFNEQ;
            case GT -> IFG;
            case LT -> IFL;
            case GTE -> IFGEQ;
            case LTE -> IFLEQ;
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
            case IFNEQ -> "!=";
            case IFGEQ -> ">=";
            case IFLEQ -> "<=";
            case IFG -> ">";
            case IFL -> "<";
            case GOTO -> "goto";
            case RET -> "ret";
            case CALL -> "call";
            case SYSCALL -> "syscall";
            case ADD_PARAM -> "addp";
            case SAVE_CONTEXT -> "savec";
            case LOAD_CONTEXT -> "loadc";
        };
    }
}
