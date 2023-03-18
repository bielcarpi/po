package entities;

import java.util.HashMap;

public class SymbolTable {

    private HashMap<String, SymbolTableEntry> map;

    public void insert(SymbolTableEntry entry){
    }

    public boolean delete(String entryId){
        return false;
    }

    public SymbolTableEntry lookup(String entryId){
        return null;
    }
}
