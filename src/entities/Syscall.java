package entities;

import java.util.ArrayList;

public enum Syscall {
    PRINT_INT("print", TokenType.INT, 1),
    PRINT_STR("prints", TokenType.STRING, 4),
    READ_INT("read", null, 5);

    private final String call;
    private final int id;

    private final TokenType argType;

    private Syscall(String call, TokenType argType, int id) {
        this.argType = argType;
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
                //After print int, print a newline
                entries.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, "a0", "0xA", TACType.EQU));
                entries.add(new TACEntry(11, TACType.SYSCALL));
                entries.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, null, "0", TACType.RET));
            }
            case PRINT_STR -> {
                entries.add(new TACEntry(id, TACType.SYSCALL));
                entries.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, null, "0", TACType.RET));
            }
            case READ_INT -> {
                entries.add(new TACEntry(id, TACType.SYSCALL));
                entries.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, null, "v0", TACType.RET));
            }
        }

        return entries;
    }

    public static int getID(Syscall syscall) {
        return syscall.id;
    }
}
