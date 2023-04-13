package entities;

public enum ErrorType {
    FILE_NOT_FOUND,
    VARIABLE_UNDECLARED,
    COMMENT_NOT_CLOSED,
    TOKEN_LIST_ERROR,
    BUFFER_LENGTH_ERROR,
    SYNTAX_ERROR,
    ;

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
        return type == FILE_NOT_FOUND;
    }
}
