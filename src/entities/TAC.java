package entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The TAC class is a data structure that holds all TACEntries
 * Internally, it is a map of String to ArrayList of TACBlocks
 *  (i.e. a map of function names to a list of TACBlocks)
 *
 * @see TACEntry
 * @see TACBlock
 * @see TACType
 */
public class TAC {

    private final HashMap<String, ArrayList<TACBlock>> entries;

    /**
     * Default constructor
     */
    public TAC() {
        entries = new HashMap<>();
    }


    /**
     * Add a TACBlock to the TAC
     * @param func The name of the function the block belongs to
     * @param block The block to add
     */
    public void add(String func, TACBlock block) {
        if (!entries.containsKey(func))
            entries.put(func, new ArrayList<>());

        entries.get(func).add(block);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String funcName : entries.keySet()) {
            sb.append(funcName).append(":\n");
            for (TACBlock block : entries.get(funcName))
                sb.append(block.toString()).append("\n");
        }
        return sb.toString();
    }
}
