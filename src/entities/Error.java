package entities;

public class Error {
    private ErrorType type;
    private String message;
    private int line;
    private int column;

    public Error(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "error: %s [%s], Column: %d. Row: %d\n".formatted(message, type, line, column);
    }

    public boolean isCritical() {
        return type == ErrorType.FILE_NOT_FOUND || type == ErrorType.SEGMENTATION_FAULT;
    }
}
