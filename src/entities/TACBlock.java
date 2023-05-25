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
     * @param requiresId Whether the block requires a unique ID
     */
    public TACBlock(boolean requiresId){
        if(requiresId) blockNum = ++numBlocks;
        else blockNum = -1;

        blockEntries = new ArrayList<>();
    }

    /**
     * Add a TACEntry to the block
     * @param entry The entry to add
     */
    public void add(TACEntry entry){
        if(entry == null) return;
        blockEntries.add(entry);
    }

    /**
     * Returns the list of TACEntries in the block
     * @return the list of TACEntries in the block
     */
    public ArrayList<TACEntry> getEntries(){
        return blockEntries;
    }

    /**
     * Returns the block number
     * @return the block number
     */
    public int getBlockNum() {
        return blockNum;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        //If the block requires an ID, add it to the string
        if(blockNum != -1) sb.append("E").append(blockNum).append(":\n");

        for(TACEntry entry: blockEntries)
            sb.append('\t').append(entry.toString()).append("\n");

        return sb.toString();
    }
}
