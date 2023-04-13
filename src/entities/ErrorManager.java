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

    public void addError(Error e) {
        errors.add(e);

        if (ErrorType.isCritical(e.getType())){
            printErrors();
            System.out.println("Critical error detected. Exiting...");
            System.exit(1);
        }

    }

    public void printErrors() {
        if (errors.size() == 0){
            System.out.println("\n\nNo errors found.");
            return;
        }

        System.out.println("\n\n" + errors.size() + " Errors found:");
        for (Error error: errors)
            System.out.println(error.toString());
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
