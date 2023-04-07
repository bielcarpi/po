package syntax_analysis;

import java.util.HashMap;
import java.util.Map;

public class ParsingTable {

    /**
     * Each ParsingTableTuple maps to a Production.
     * ParsingTableTuple has two values: The producer (String) and the terminal (TokenType)
     */
    private HashMap<ParsingTableTuple, Production> hashMap;

    /**
     * Default ParsingTable Constructor
     */
    public ParsingTable(){
        hashMap = new HashMap<>();
    }

    /**
     * Adds the ParsingTableTuple with the Production to the ParsingTable
     * @param tuple The tuple to add
     * @param p The Production for that tuple
     * @return Whether the tuple already existed or not. If it already existed, it won't be added
     */
    public boolean addProduction(ParsingTableTuple tuple, Production p){
        if(hashMap.containsKey(tuple)) return false;

        hashMap.put(tuple, p);
        return true;
    }


    /**
     * Returns the Production for the ParsingTableTuple provided, or {@code null} if
     *  there is no ParsingTableTuple like the one provided
     * @param tuple The tuple that represents the Production to get
     * @return The Production for that ParsingTableTuple, or {@code null} if there is no entry
     */
    public Production getProduction(ParsingTableTuple tuple){
        return hashMap.get(tuple);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<ParsingTableTuple, Production> e: hashMap.entrySet())
            sb.append(e.getKey()).append("\n").append(e.getValue());

        return sb.toString();
    }
}
