package entities;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * The SymbolTableVariableEntry represents a {@link SymbolTableEntry} for Variables.
 *
 * @see SymbolTableEntry
 * @see SymbolTable
 */
public class SymbolTableVariableEntry extends SymbolTableEntry{

    private final int size; //Array size

    /**
     * The programID is the ID of this variable (the register where it is stored: e.g. t0, t1... t7) in the running program
     * It will be assigned later on, by the MIPSGenerator
     */
    private int programID;

    private static int sProgramID = 0;


    /**
     * SymbolTableVariableEntry Constructor
     * @param id The ID of the entry
     * @param scope The scope of the Variable
     * @param size The size of the variable (or 1 if it is not an array)
     */
    public SymbolTableVariableEntry(final @NotNull String id, final @NotNull String scope, final @NotNull TokenType type, final int size) {
        super(id, scope, type);
        this.size = size;
        programID = programID++;
    }

    /**
     * Returns the size of the array (or 1 if it's a simple variable)
     * @return The size of the array or 1 if it's not an array
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the program ID of the variable
     * @param programID The program ID of the variable
     */
    public void setProgramID(int programID) {
    	this.programID = programID;
    }

    /**
     * Returns the program ID of the variable if it has been set, otherwise returns the normal ID
     * @return The program ID of the variable
     */
    public int getProgramID() {
    	return programID;
    }


    @Override
    public String toString() {
        return "[Variable] ID: " + super.getId() + ", Type: " + super.getType() + ", Scope: " + super.getScope();
    }
}
