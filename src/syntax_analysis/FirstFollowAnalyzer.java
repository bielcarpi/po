package syntax_analysis;

import entities.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FirstFollowAnalyzer {

    private static void getFirsts(ArrayList<TokenType> firsts, Object next, HashMap<String, Production> map){
        if(next instanceof TokenType){ //If next is final, we found the first
            firsts.add((TokenType) next);
        }
        else{ //Else, expand the non-terminal found
            Production p = map.get((String) next);
            List<ArrayList<Object>> derivations = p.getDerived();
            for(ArrayList<Object> derivation: derivations)
                getFirsts(firsts, derivation.get(0), map);
        }
    }

    /**
     * Fills the provided Parsing Table, given the List of Productions
     * @param pt The ParsingTable to fill
     * @param productions The productions to analyze
     * @return Whether grammar is ambiguous or not
     */
    public static boolean fillParsingTable(ParsingTable pt, List<Production> productions){
        HashMap<String, Production> map = new HashMap<>();
        for(Production p: productions) map.put(p.getProducer(), p);

        for(Production p: productions){
            List<ArrayList<Object>> derivations = p.getDerived();
            for(int i = 0; i < derivations.size(); i++){
                ArrayList<TokenType> firsts = new ArrayList<>();
                getFirsts(firsts, derivations.get(i).get(0), map);
                p.setFirsts(firsts, i);
            }
        }

        /*
        for(int i = 0; i < productions.size(); i++){
            List<ArrayList<Object>> derivations = productions.get(i).getDerived();
            // We search for the first of each derivation
            for(int j = 0; j < derivations.size(); j++){
                // We analize each part of any possible derivation of the production
                Object firstObject = derivations.get(j).get(0);
                ArrayList<TokenType> firsts = new ArrayList<>();
                if(firstObject instanceof TokenType){ //Terminal
                    // Addition of the symbol into the Hash Table
                    firsts.add((TokenType) firstObject);
                    productions.get(i).setFirsts(firsts, j);
                }
                else{ //Non-Terminal
                    getFirsts(firsts, (String) firstObject, map);
                }
            }
        }
         */

        //productions.get(0).setFirsts();
        //productions.get(0).setFollows();

        //Example
        //pt.addProduction(new ParsingTableTuple("E", TokenType.OPEN_PAREN), productions.get(0));
            //

        return true;
    }


}
