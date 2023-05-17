package entities;

import java.util.ArrayList;

/**
 * Represents a block of TACEntries
 * A block is a sequence of TACEntries that ends with a GOTO
 *
 * @see TAC
 * @see TACEntry
 * @see TACType
 */
public class TACBlock {

    /**
     * Keep track of the number of blocks created
     */
    private static int numBlocks = -1;

    private final int blockNum;
    private final ArrayList<TACEntry> blockEntries;

    /**
     * Default constructor
     */
    public TACBlock(){
        blockNum = numBlocks++;
        blockEntries = new ArrayList<>();
    }

    /**
     * Add a TACEntry to the block
     * @param entry
     */
    public void add(TACEntry entry){
        blockEntries.add(entry);
    }

    /**
     * Returns the list of TACEntries in the block
     * @return the list of TACEntries in the block
     */
    public ArrayList<TACEntry> getEntries(){
        return blockEntries;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("E").append(blockNum).append(":\n");
        for(TACEntry entry: blockEntries)
            sb.append(entry.toString()).append("\n");

        return sb.toString();
    }
}
