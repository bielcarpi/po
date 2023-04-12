package syntax_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParsingTable {

    /**
     * Each ParsingTableTuple maps to a ParsingTableValue (Production + num of the derivation).
     * ParsingTableTuple has two values: The producer (String) and the terminal (TokenType)
     */
    private final HashMap<ParsingTableKey, ParsingTableValue> map;

    /**
     * Default ParsingTable Constructor
     */
    public ParsingTable(){
        map = new HashMap<>();
    }

    /**
     * Adds the ParsingTableKey with the Production to the ParsingTable
     * @param key The Key to add
     * @param value The Production for that tuple
     */
    public void addProduction(ParsingTableKey key, ParsingTableValue value){
        if(map.containsKey(key)){
            //TODO: Manage critical error
            System.out.println("Error filling the Parsing Table. Grammar is ambiguous.");
            return;
        }

        map.put(key, value);
    }


    /**
     * Returns the Production for the ParsingTableKey provided, or {@code null} if
     *  there is no ParsingTableKey like the one provided
     * @param tuple The tuple that represents the Production to get
     * @return The Production for that ParsingTableTuple, or {@code null} if there is no entry
     */
    public ParsingTableValue getProduction(ParsingTableKey tuple){
        return map.get(tuple);
    }

    public Production getProduction(String production){
        for(Map.Entry<ParsingTableKey, ParsingTableValue> e: map.entrySet())
            if(e.getValue().production().getProducer().equals(production))
                return e.getValue().production();

        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<ParsingTableKey, ParsingTableValue> e: map.entrySet())
            sb.append(e.getKey()).append("\t").append(e.getValue()).append("\n");

        return sb.toString();
    }
}
