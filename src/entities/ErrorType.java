package entities;

public enum ErrorType {
    FILE_NOT_FOUND,
    ;

    public static String getMessage(ErrorType type) {
        switch (type) {
            case FILE_NOT_FOUND:
                return "File not found";
            default:
                return "Unknown error";
        }
    }
}
