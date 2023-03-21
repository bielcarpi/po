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

    private final HashMap<String, SymbolTableEntry> map;

    /**
     * Symbol Table Constructor
     */
    public SymbolTable(){
        map = new HashMap<>();
    }

    /**
     * Inserts a SymbolTableEntry to this SymbolTable
     * @param entry The entry to insert
     */
    public void insert(@NotNull SymbolTableEntry entry) throws DuplicateEntryException{
        if(map.containsKey(entry.getId())) throw new DuplicateEntryException(entry.getId());
        map.put(entry.getId(), entry);
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
    public SymbolTableEntry lookup(@NotNull String entryId){
        return map.get(entryId);
    }
}
