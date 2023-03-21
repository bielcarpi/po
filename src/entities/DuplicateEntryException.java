package entities;

/**
 * DuplicateEntryException is an {@link Exception} which will be thrown whenever a duplicate entry
 * (in the current scope) is inserted in the SymbolTable.
 *
 * @see SymbolTable
 * @see SymbolTableEntry
 */
public class DuplicateEntryException extends Exception {

    private final String duplicateId;

    /**
     * DuplicateEntryException Constructor
     * @param duplicateId The ID that is duplicated
     */
    public DuplicateEntryException(String duplicateId){
        this.duplicateId = duplicateId;
    }

    /**
     * Returns the Duplicate ID
     * @return The Duplicate ID
     */
    public String getDuplicateId(){
        return duplicateId;
    }
}
