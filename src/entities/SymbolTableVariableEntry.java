package entities;

/**
 * The SymbolTableVariableEntry represents a {@link SymbolTableEntry} for Variables.
 *
 * @see SymbolTableEntry
 * @see SymbolTable
 */
public class SymbolTableVariableEntry extends SymbolTableEntry{

    private final int size; //Array size

    /**
     * SymbolTableVariableEntry Constructor
     * @param id The ID of the entry
     * @param scope The scope of the Variable
     * @param size The size of the variable (or 1 if it is not an array)
     */
    public SymbolTableVariableEntry(final String id, final String scope, final int size) {
        super(id, scope);
        this.size = size;
    }
}
