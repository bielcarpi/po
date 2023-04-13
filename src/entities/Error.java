package entities;

public class Error {
    private final ErrorType type;
    private final String message;
    private final int line;
    private final int column;

    public Error(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column;
    }

    public Error(ErrorType type, int line, int column) {
        this.type = type;
        this.message = ErrorType.getMessage(type);
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
        return "Error on line %d column %d: [%s] %s".formatted(line, column, type, message);
    }
}
