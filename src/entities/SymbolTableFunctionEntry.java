package entities;

public class SymbolTableFunctionEntry extends SymbolTableEntry{

    private final int arguments;

    public SymbolTableFunctionEntry(final String id, final String scope, final int arguments) {
        super(id, scope);
        this.arguments = arguments;
    }
}
