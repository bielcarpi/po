package entities;

/**
 * The TACEntry class represents a single entry in TAC
 * It has a source, and one or two arguments
 *
 * @see TAC
 * @see TACType
 * @see TACBlock
 */
public class TACEntry {
    private final TACType type;
    private final String src, arg1, arg2;
    private int blockNum; //Optional argument for GOTOs

    /**
     * Constructor for TACEntry
     * @param src The source of the entry
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @param type The type of the entry
     */
    public TACEntry(String src, String arg1, String arg2, TACType type) {
        this.type = type;

        if(src != null && SymbolTable.getInstance().lookup(src) != null)
            this.src = ((SymbolTableVariableEntry)SymbolTable.getInstance().lookup(src)).getProgramID();
        else
            this.src = src;

        if(arg2 != null && SymbolTable.getInstance().lookup(arg1) != null)
            this.arg1 = ((SymbolTableVariableEntry)SymbolTable.getInstance().lookup(arg1)).getProgramID();
        else
            this.arg1 = arg1;

        if(arg2 != null && SymbolTable.getInstance().lookup(arg2) != null)
            this.arg2 = ((SymbolTableVariableEntry)SymbolTable.getInstance().lookup(arg2)).getProgramID();
        else
            this.arg2 = arg2;
    }

    /**
     * Constructor for TACEntry with only one argument
     * @param src The source of the entry
     * @param arg1 The first argument
     * @param type The type of the entry
     */
    public TACEntry(String src, String arg1, TACType type) {
        this(src, arg1, null, type);
    }

    /**
     * Constructor for TACEntry without source, two arguments and a block number
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @param type The type of the entry
     * @param blockNum The block number
     */
    public TACEntry(String arg1, String arg2, int blockNum, TACType type) {
        this(null, arg1, arg2, type);
        this.blockNum = blockNum;
    }

    /**
     * Constructor for TACEntry with one argument and a block number (goto statement, for example)
     * @param blockNum The block number
     * @param type The type of the entry
     */
    public TACEntry(int blockNum, TACType type) {
        this(null, null, null, type);
        this.blockNum = blockNum;
    }

    /**
     * Returns the type of the entry
     * @return the type of the entry
     */
    public TACType getType() {
        return type;
    }

    /**
     * Returns the source of the entry
     * @return the source of the entry
     */
    public String getSrc() {
        return src;
    }

    /**
     * Returns the first argument of the entry
     * @return the first argument of the entry
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * Returns the second argument of the entry
     * @return the second argument of the entry
     */
    public String getArg2() {
        return arg2;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(type == TACType.ADD || type == TACType.SUB || type == TACType.MUL || type == TACType.DIV || type == TACType.AND || type == TACType.OR){
            sb.append(src).append(" = ").append(arg1).append(" ").append(type).append(" ").append(arg2);
        }
        else if(type == TACType.EQU){
            sb.append(src).append(" = ").append(arg1);
        }
        else if(type == TACType.IFG || type == TACType.IFL || type == TACType.IFGEQ || type == TACType.IFLEQ){
            sb.append("if ").append(arg1).append(" ").append(type).append(" ").append(arg2).append(" ").append(TACType.GOTO).append(" E").append(blockNum);
        }
        else if(type == TACType.GOTO){
            sb.append(TACType.GOTO).append(" E").append(blockNum);
        }
        else{
            sb.append("ERROR");
        }

        return sb.toString();
    }
}

