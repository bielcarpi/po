package entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    private static HashMap<String, SymbolTableEntry> map;

    private static HashSet<String> scopes = new HashSet<>();

    private static int internalIDCount = 1001;


    /**
     * Private constructor for the singleton
     */
    private SymbolTable() {
        map = new HashMap<>();
        scopes = new HashSet<>();
        scopes.add(GLOBAL_SCOPE);
    }

    /**
     * Returns the instance of this SymbolTable
     * @return The instance of this SymbolTable
     */
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
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
                    "Error, identifier " + entry.getId() + " already exists in the same scope");
            ErrorManager.getInstance().addError(error);

            return;
        }

        scopes.add(entry.getScope()); // Add the scope to the HashSet of scopes
        map.put(entry.getId() + entry.getScope(), entry);
    }


    /**
     * Returns the list of scopes in this SymbolTable
     * @return The list of scopes in this SymbolTable
     */
    public List<String> getScopes() {
        if(scopes.isEmpty()) return null;
        return scopes.stream().toList();
    }

    /**
     * Returns the list of {@link SymbolTableVariableEntry} in this SymbolTable, filtered by scope
     *  e.g. if scope is "main", it will return all the variable entries (not functions) in the main scope
     * @param scope The scope to filter by
     * @return The list of {@link SymbolTableVariableEntry} in this SymbolTable, filtered by scope
     */
    public ArrayList<SymbolTableVariableEntry> getVariableEntries(String scope){
        if(!scopes.contains(scope)) return null;

        ArrayList<SymbolTableVariableEntry> entries = new ArrayList<>();
        for (SymbolTableEntry entry : map.values())
            if(entry.getScope().equals(scope) && entry instanceof SymbolTableVariableEntry) entries.add((SymbolTableVariableEntry) entry);

        return entries.isEmpty() ? null : entries;
    }

    /**
     * Returns the list of {@link SymbolTableVariableEntry} in this SymbolTable
     * @return The list of {@link SymbolTableVariableEntry} in this SymbolTable
     */
    public ArrayList<SymbolTableVariableEntry> getVariableEntries(){
        ArrayList<SymbolTableVariableEntry> entries = new ArrayList<>();
        for (SymbolTableEntry entry : map.values())
            if(entry instanceof SymbolTableVariableEntry) entries.add((SymbolTableVariableEntry) entry);

        return entries.isEmpty() ? null : entries;
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

        //If it's found, and it's a variable, increment the number of times it's used
        if (ste instanceof SymbolTableVariableEntry)
            ((SymbolTableVariableEntry) ste).incrementNumTimesUsed();

        return ste;
    }

    public String getNewInternalID() {
        return "^" + internalIDCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SymbolTableEntry entry : map.values())
            sb.append(entry.toString()).append("\n");

        return sb.toString();
    }
}
