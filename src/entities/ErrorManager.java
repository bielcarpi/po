package entities;

import java.util.ArrayList;

public class ErrorManager {
    private static ErrorManager instance = null;
    private static ArrayList<Error> errors;

    private ErrorManager() {
    }

    public static ErrorManager getInstance() {
        if (instance == null) {
            instance = new ErrorManager();
            errors = new ArrayList<>();
        }
        return instance;
    }

    public void addError(ErrorType type, int line, int column) {
        String message = ErrorType.getMessage(type);
        Error error = new Error(type, message, line, column);
        System.out.println(error.toString());

        if (error.isCritical())
            System.exit(1);

    }

    public void printErrors() {
        for (Error error : errors) {
            System.out.println(error.toString());
        }
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
