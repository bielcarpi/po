package entities;

/**
 * The Error class represents an error that occurred during the compilation process.
 * It contains the type of the error, the message, and the line and column where it occurred.
 *
 * @see ErrorType
 * @see ErrorManager
 */
public class Error {
    private final ErrorType type;
    private final String message;
    private final int line;
    private final int column;

    /**
     * Default constructor
     * @param type the type of the error
     * @param message the message of the error
     * @param line the line where the error occurred
     * @param column the column where the error occurred
     */
    public Error(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column;
    }

    /**
     * Constructor for errors that don't have a line and column
     * @param type the type of the error
     * @param message the message of the error
     */
    public Error(ErrorType type, String message) {
        this.type = type;
        this.message = message;
        this.line = -1;
        this.column = -1;
    }

    /**
     * Constructor for errors that don't have a message
     * @param type the type of the error
     * @param line the line where the error occurred
     * @param column the column where the error occurred
     */
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
