package entities;

import org.jetbrains.annotations.NotNull;

/**
 * The SymbolTableVariableEntry represents a {@link SymbolTableEntry} for Variables.
 *
 * @see SymbolTableEntry
 * @see SymbolTable
 */
public class SymbolTableVariableEntry extends SymbolTableEntry{

    private final int size; //Array size

    /**
     * The registerID contains the register where this is stored (e.g. t0, t1... t7) in the running program
     * It will be assigned later on, by the MIPSGenerator, or -1 if it has no register and has to be loaded
     * from memory and stored back to memory
     */
    private int registerID;

    /**
     * The numTimesUsed contains the number of times this variable is being used in code.
     * The most used variables will be stored in registers, and the least used will be stored in memory
     */
    private int numTimesUsed;

    private final boolean isParam;

    private final String strValue;

    /**
     * SymbolTableVariableEntry Constructor
     * @param id The ID of the entry
     * @param scope The scope of the Variable
     * @param size The size of the variable (or 1 if it is not an array)
     */
    public SymbolTableVariableEntry(final @NotNull String id, final @NotNull String scope, final @NotNull TokenType type, final int size) {
        super(id, scope, type);
        this.size = size;
        registerID = -1;
        numTimesUsed = 0;
        isParam = false;
        strValue = null;
    }

    /**
     * SymbolTableVariableEntry Default Constructor
     * @param id The ID of the entry
     * @param scope The scope of the Variable
     * @param type The type of the Variable
     * @param size The size of the variable (or 1 if it is not an array)
     * @param paramNum The number of the parameter (if it is a parameter of a function)
     */
    public SymbolTableVariableEntry(final @NotNull String id, final @NotNull String scope, final @NotNull TokenType type,
                                    final int size, final int paramNum) {
        super(id, scope, type);
        this.size = size;
        registerID = paramNum;
        isParam = true;
        numTimesUsed = 0;
        strValue = null;
    }

    /**
     * SymbolTableVariableEntry Constructor for Strings
     * @param id The ID of the entry
     * @param scope The scope of the Variable
     * @param type The type of the Variable
     * @param strValue The value of the string
     */
    public SymbolTableVariableEntry(final @NotNull String id, final @NotNull String scope, final @NotNull TokenType type,
                                    final String strValue) {
        super(id, scope, type);
        this.size = 1;
        registerID = -1;
        isParam = false;
        numTimesUsed = 0;
        this.strValue = strValue;
    }

    /**
     * Returns the size of the array (or 1 if it's a simple variable)
     * @return The size of the array or 1 if it's not an array
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the register ID of this variable (only on most used variables)
     * @param registerID The new register ID of the variable
     */
    public void setRegisterID(int registerID) {
    	this.registerID = registerID;
    }

    /**
     * Returns the register of the variable if it has been set, otherwise returns -1
     * @return The register of the variable if it has been set, otherwise returns -1
     */
    public int getRegisterID() {
    	return registerID;
    }

    /**
     * Returns the number of times this variable is being used in code
     * @return The number of times this variable is being used in code
     */
    public int getNumTimesUsed() {
        return numTimesUsed;
    }

    /**
     * Increments the number of times this variable is being used in code
     */
    public void incrementNumTimesUsed() {
        numTimesUsed++;
    }

    /**
     * Returns whether this variable is a parameter of a function or not
     * @return Whether this variable is a parameter of a function or not
     */
    public boolean isParameter(){
        return isParam;
    }

    public String getStringValue() {
        return strValue;
    }


    @Override
    public String toString() {
        return "[Variable] ID: " + super.getId() + ", Type: " + super.getType() + ", Scope: " + super.getScope()
                + ", Times Used: " + numTimesUsed + ", Register Assigned: " +
                (isParameter() ? "$a" + registerID : (registerID == -1 ? "NULL" : "$t" + registerID) +
                        (strValue == null ? "\n" : ", Value: " + strValue + "\n"));
    }
}
