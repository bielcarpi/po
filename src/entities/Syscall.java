package entities;

import java.util.ArrayList;

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

    /**
     * Returns the list of TACEntries for a given syscall,
     *  which will need to be put later on into the syscall block
     * @return the list of TACEntries for a given syscall
     */
    public ArrayList<TACEntry> getTACEntries(){
        ArrayList<TACEntry> entries = new ArrayList<>();
        switch (this) {
            case PRINT_INT -> {
                entries.add(new TACEntry(id, TACType.SYSCALL));
                entries.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, null, "0", TACType.RET));
            }
            case PRINT_STR -> {
                //entries.add(new TACEntry(TACType.SYSCALL, "4"));
            }
        }

        return entries;
    }

    public static int getID(Syscall syscall) {
        return syscall.id;
    }
}
