package entities;

public class Error {
    private ErrorType type;
    private String message;
    private int line;
    private int column;
    private boolean isCritical;

    public Error(ErrorType type, String message, int line, int column, boolean isCritical) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column;
        this.isCritical = isCritical;
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

    public boolean isCritical() {
        return isCritical;
    }

    @Override
    public String toString() {
        return "error: %s [%s], Column: %d. Row: %d\n".formatted(message, type, line, column);
    }
}
