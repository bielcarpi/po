package entities;

public abstract class SymbolTableEntry {

    private final String id;
    private final String scope;

    public SymbolTableEntry(final String id, final String scope){
        this.id = id;
        this.scope = scope;
    }

    public String getId(){
        return id;
    }
}
