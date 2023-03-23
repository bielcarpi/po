package entities;

import org.jetbrains.annotations.NotNull;

/**
 * The SymbolTableEntry class represents an entry in a {@link SymbolTable}.
 * This class is {@code abstract}. Hence, specific inheritances will be created.
 *
 * @see SymbolTable
 */
public abstract class SymbolTableEntry {

    private final String id;
    private final String scope;

    /**
     * SymbolTableEntry Constructor
     * @param id The ID of the entry
     * @param scope The scope of the entry
     */
    public SymbolTableEntry(@NotNull final String id, @NotNull final String scope){
        this.id = id;
        this.scope = scope;
    }

    /**
     * Returns the ID of the entry
     * @return The ID of the entry
     */
    @NotNull
    public String getId(){
        return id;
    }

    /**
     * Returns the scope of the entry
     * @return The scope of the entry
     */
    @NotNull
    public String getScope() {
        return scope;
    }
}
