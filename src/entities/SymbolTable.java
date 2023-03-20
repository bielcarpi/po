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

    private HashMap<String, SymbolTableEntry> map;

    /**
     * Inserts a SymbolTableEntry to this SymbolTable
     * @param entry The entry to insert
     */
    public void insert(@NotNull SymbolTableEntry entry){
    }

    /**
     * Deletes a SymbolTableEntry from this SymbolTable, given its ID
     * @param entryId The ID of the entry to delete
     * @return Whether the entry was deleted or not
     */
    public boolean delete(@NotNull String entryId){
        return false;
    }

    /**
     * Search a SymbolTableEntry in this SymbolTable, given the ID
     * @param entryId The ID of the SymbolTableEntry to look for
     * @return The SymbolTableEntry found or {@code null}
     */
    @Nullable
    public SymbolTableEntry lookup(@NotNull String entryId){
        return null;
    }
}
