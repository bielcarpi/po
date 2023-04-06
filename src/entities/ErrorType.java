package entities;

public enum ErrorType {
    FILE_NOT_FOUND,
    SEGMENTATION_FAULT,
    VARIABLE_UNDECLARED,
    COMMENT_NOT_CLOSED,
    TOKEN_LIST_ERROR,
    BUFFER_LENTGH_ERROR,
    ;

    public static String getMessage(ErrorType type) {
        switch (type) {
            case FILE_NOT_FOUND:
                return "File not found";
            // TODO: Define this error
            case SEGMENTATION_FAULT:
                // Critical error -> Figure out how to exit the program
                return "";
            // TODO: Define this error
            case VARIABLE_UNDECLARED:
                return "";
            case COMMENT_NOT_CLOSED:
                return "Long comment not closed properly";
            case TOKEN_LIST_ERROR:
                return "The token hasn't been added to the list";
            case BUFFER_LENTGH_ERROR:
                return "End fo the execution. Warning: Token buffer is not empty!";
            default:
                return "Unknown error";
        }
    }
}
