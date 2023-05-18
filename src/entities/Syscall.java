package entities;

public enum Syscall {
    PRINT_INT("print", 1),
    PRINT_STR("print", 4);

    private final String call;
    private final int id;
    private Syscall(String call, int id) {
        this.call = call;
        this.id = id;
    }

    public static boolean isSyscall(String func) {
        for(Syscall s : Syscall.values())
            if(s.call.equals(func)) return true;

        return false;
    }

    public static Syscall get(String call){
        for(Syscall s : Syscall.values())
            if(s.call.equals(call)) return s;

        return null;
    }

    public static int getID(Syscall syscall) {
        return syscall.id;
    }
}
