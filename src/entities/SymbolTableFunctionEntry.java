package entities;

import org.jetbrains.annotations.NotNull;

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
    public SymbolTableFunctionEntry(final @NotNull String id, final @NotNull String scope, final int arguments) {
        super(id, scope);
        this.arguments = arguments;
    }


    /**
     * Returns the number of arguments of this function
     * @return The number of arguments of this function
     */
    public int getArguments() {
        return arguments;
    }
}
