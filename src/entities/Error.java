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
}
