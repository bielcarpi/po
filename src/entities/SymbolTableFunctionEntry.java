package entities;

/**
 * The SymbolTableFunctionEntry represents a {@link SymbolTableEntry} for Functions.
 *
 * @see SymbolTableEntry
 * @see SymbolTable
 */
public class SymbolTableFunctionEntry extends SymbolTableEntry{

    private final int arguments;

    /**
     * SymbolTableFunctionEntry Constructor
     * @param id The ID of the entry
     * @param scope The scope of the Function
     * @param arguments The number of arguments in the Function
     */
    public SymbolTableFunctionEntry(final String id, final String scope, final int arguments) {
        super(id, scope);
        this.arguments = arguments;
    }
}
