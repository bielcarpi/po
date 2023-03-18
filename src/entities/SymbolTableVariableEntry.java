package entities;

public class SymbolTableVariableEntry extends SymbolTableEntry{

    private final int size; //Array size

    public SymbolTableVariableEntry(final String id, final String scope, final int size) {
        super(id, scope);
        this.size = size;
    }
}
