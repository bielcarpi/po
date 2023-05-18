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
    private final String dest, arg1, arg2;
    private final String scope; //We also need scope in order to identify the variable later on
    private int blockNum; //Optional argument for GOTOs

    /**
     * Constructor for TACEntry
     * @param  dest The source of the entry
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @param type The type of the entry
     */
    public TACEntry(String scope, String dest, String arg1, String arg2, TACType type) {
        this.scope = scope;
        this.type = type;
        this.dest =  dest;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     * Constructor for TACEntry with only one argument
     * @param dest The source of the entry
     * @param arg1 The first argument
     * @param type The type of the entry
     */
    public TACEntry(String scope, String dest, String arg1, TACType type) {
        this(scope, dest, arg1, null, type);
    }

    /**
     * Constructor for TACEntry without source, two arguments and a block number
     * @param arg1 The first argument
     * @param arg2 The second argument
     * @param type The type of the entry
     * @param blockNum The block number
     */
    public TACEntry(String scope, String arg1, String arg2, int blockNum, TACType type) {
        this(scope, null, arg1, arg2, type);
        this.blockNum = blockNum;
    }

    /**
     * Constructor for TACEntry with one argument and a block number (goto statement, for example)
     * @param blockNum The block number
     * @param type The type of the entry
     */
    public TACEntry(int blockNum, TACType type) {
        this(null, null, null, null, type);
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
    public String getDest() {
        return dest;
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

    /**
     * Returns the scope of the dest
     * @return The scope of the dest
     */
    public String getScope(){
        return scope;
    }

    /**
     * Returns the block number of the entry
     * @return the block number of the entry
     */
    public int getBlockNum() {
        return blockNum;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(type == TACType.ADD || type == TACType.SUB || type == TACType.MUL || type == TACType.DIV || type == TACType.AND || type == TACType.OR){
            sb.append(dest).append(" = ").append(arg1).append(" ").append(type).append(" ").append(arg2);
        }
        else if(type == TACType.EQU){
            sb.append(dest).append(" = ").append(arg1);
        }
        else if(type == TACType.IFG || type == TACType.IFL || type == TACType.IFGEQ || type == TACType.IFLEQ || type == TACType.IFEQU || type == TACType.IFNEQ){
            sb.append("if ").append(arg1).append(" ").append(type).append(" ").append(arg2).append(" ").append(TACType.GOTO).append(" E").append(blockNum);
        }
        else if(type == TACType.GOTO){
            if(dest != null) sb.append(TACType.GOTO).append(" ").append(dest);
            else sb.append(TACType.GOTO).append(" E").append(blockNum);
        }
        else if(type == TACType.RET || type == TACType.CALL){
            sb.append(type).append(" ").append(arg1);
        }
        else if(type == TACType.SYSCALL){
            sb.append(type).append(" ").append(blockNum);
        }
        else{
            sb.append("ERROR");
        }

        return sb.toString();
    }
}

