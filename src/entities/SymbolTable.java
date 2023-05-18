package entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * The SymbolTable class represents the symbol table of the compiler.
 * It lets you insert, lookup and delete {@link SymbolTableEntry} in a fast, efficient way.
 *
 * @see SymbolTableEntry
 */
public class SymbolTable {

    public static final String GLOBAL_SCOPE = "global";
    public static final String MAIN_SCOPE = "main";

    private static SymbolTable instance = null;
    private static HashMap<String, SymbolTableEntry> map = null;


    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
            map = new HashMap<>();
        }
        return instance;
    }

    /**
     * Inserts a SymbolTableEntry to this SymbolTable
     * @param entry The entry to insert
     */
    public void insert(@NotNull SymbolTableEntry entry) {
        // Check if already exists and put to error manager
        if (map.containsKey(entry.getId() + GLOBAL_SCOPE) || map.containsKey(entry.getId() + entry.getScope())) {
            Error error = new entities.Error(ErrorType.REPEATED_SCOPE_ENTRY,
                    "Error, variable " + entry.getId() + " already exists in the same scope");
            ErrorManager.getInstance().addError(error);

            return;
        }

        map.put(entry.getId() + entry.getScope(), entry);
    }

    /**
     * Deletes a SymbolTableEntry from this SymbolTable, given its ID
     * @param entryId The ID of the entry to delete
     * @return The entry that was deleted, or {@code null} if there was no entry with the provided ID
     */
    @Nullable
    public SymbolTableEntry delete(@NotNull String entryId){
        return map.remove(entryId);
    }

    /**
     * Search a SymbolTableEntry in this SymbolTable, given the ID
     * @param entryId The ID of the SymbolTableEntry to look for
     * @return The SymbolTableEntry found or {@code null}
     */
    @Nullable
    public SymbolTableEntry lookup(@NotNull String entryId, @NotNull String scope){
        SymbolTableEntry ste = map.get(entryId + scope);
        if (ste == null) ste = map.get(entryId + GLOBAL_SCOPE);
        return ste;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SymbolTableEntry entry : map.values()) {

            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }
}
