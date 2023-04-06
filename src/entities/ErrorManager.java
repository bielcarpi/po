package entities;

public class ErrorManager {
    private static ErrorManager instance = null;

    private ErrorManager() {
    }

    public static ErrorManager getInstance() {
        if (instance == null) {
            instance = new ErrorManager();
        }
        return instance;
    }

    public void addError(Error error) {
        System.out.println(error);
    }
}
