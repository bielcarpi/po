package entities;

/**
 * The ErrorType enum represents the type of error that occurred during the compilation process.
 *
 * @see Error
 * @see ErrorManager
 */
public enum ErrorType {
    FILE_NOT_FOUND,
    VARIABLE_UNDECLARED,
    COMMENT_NOT_CLOSED,
    TOKEN_LIST_ERROR,
    BUFFER_LENGTH_ERROR,
    SYNTAX_ERROR,
    TAC_GENERATION_ERROR,
    TAC_OPTIMIZATION_ERROR,
    MIPS_GENERATION_ERROR,
    REPEATED_SCOPE_ENTRY,
    MISMATCHED_TYPE_OPERATION,
    TOO_MANY_ARGUMENTS,
    FUNCTION_UNDECLARED,
    INVALID_SYSCALL_USE;

    public static String getMessage(ErrorType type) {
        return switch (type) {
            case FILE_NOT_FOUND -> "File not found"; //Critical
            case VARIABLE_UNDECLARED -> "There is an undeclared variable";
            case COMMENT_NOT_CLOSED -> "Multiline comment not closed properly";
            case TOKEN_LIST_ERROR -> "The token hasn't been added to the list";
            case BUFFER_LENGTH_ERROR -> "End of the execution. Warning: Token buffer is not empty!";
            default -> "Unknown error";
        };
    }

    public static boolean isCritical(ErrorType type) {
        return type == FILE_NOT_FOUND || type == TAC_GENERATION_ERROR || type == TAC_OPTIMIZATION_ERROR ||
                type == MIPS_GENERATION_ERROR;
    }
}
