package entities;

public enum ErrorType {
    FILE_NOT_FOUND,
    SEGMENTATION_FAULT,
    VARIABLE_UNDECLARED,
    COMMENT_NOT_CLOSED,
    TOKEN_LIST_ERROR,
    BUFFER_LENGTH_ERROR,
    ;

    public static String getMessage(ErrorType type) {
        switch (type) {
            case FILE_NOT_FOUND:
                // Critical error
                return "File not found";
            case SEGMENTATION_FAULT:
                // Critical error
                return "Segmentation fault (core dumped)";
            case VARIABLE_UNDECLARED:
                return "There is an undeclared variable";
            case COMMENT_NOT_CLOSED:
                return "Multiline comment not closed properly";
            case TOKEN_LIST_ERROR:
                return "The token hasn't been added to the list";
            case BUFFER_LENGTH_ERROR:
                return "End of the execution. Warning: Token buffer is not empty!";
            default:
                return "Unknown error";
        }
    }
}
