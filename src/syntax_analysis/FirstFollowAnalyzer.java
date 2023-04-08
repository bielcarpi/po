package syntax_analysis;

import entities.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstFollowAnalyzer {

    /**
     * Fills the provided Parsing Table, given the List of Productions
     * @param pt The ParsingTable to fill
     * @param productions The productions to analyze
     * @return Whether grammar is ambiguous or not
     */
    public static boolean fillParsingTable(ParsingTable pt, List<Production> productions){
        HashMap<String, Production> map = new HashMap<>();
        for(Production p: productions) map.put(p.getProducer(), p);

        //Calculate Firsts
        for(Production p: productions){
            List<ArrayList<Object>> derivations = p.getDerived();
            for(int i = 0; i < derivations.size(); i++){
                ArrayList<TokenType> firsts = new ArrayList<>();
                getFirsts(firsts, derivations.get(i).get(0), map);
                p.setFirsts(firsts, i);
            }
        }

        //Calculate Follows
        for(int i = 0; i < productions.size(); i++){
            ArrayList<TokenType> follows = new ArrayList<>();
            if(i == 0) follows.add(TokenType.EOF); //In the first rule, add $
            getFollows(follows, productions.get(i).getProducer(), map);
            productions.get(i).setFollows(follows);
        }

        return true;
    }


    private static void getFollows(ArrayList<TokenType> follows, String next, HashMap<String, Production> map){

        //If we already computed follows for that 'next', add them
        if(map.get(next).getFollows() != null){
            follows.addAll(map.get(next).getFollows());
            return;
        }

        //Else, run through all productions searching for ones that can produce 'next'
        for(Production p: map.values()){
            if(p.produces(next)){

                List<ArrayList<Object>> derivations = p.getDerived();
                for(int i = 0; i < derivations.size(); i++){ //Recorrem cada derivacio
                    for(int j = 0; j < derivations.get(i).size(); j++){ //Recorrem simbols de la derivacio
                        //Si hem trobat el next
                        if(derivations.get(i).get(j) instanceof String && map.get(next).getProducer().equals(derivations.get(i).get(j))){
                            j++;
                            if(j == derivations.get(i).size()){ //No hi ha res mes darrere de next
                                if(!p.getProducer().equals(next)) //Si el que ens produeix som nosaltres mateixos, return
                                    getFollows(follows, p.getProducer(), map);
                            }
                            else{ //Si que hi ha algo mes darrere de next
                                if(derivations.get(i).get(j) instanceof TokenType){ // Es un terminal
                                    follows.add((TokenType) derivations.get(i).get(j));
                                }
                                else { // Es un no terminal
                                    Production n = map.get((String) derivations.get(i).get(j));
                                    ArrayList<TokenType> firsts = n.getAllFirsts();
                                    if(firsts != null){
                                        for(TokenType t: firsts){
                                            if(t == TokenType.VOID) getFollows(follows, p.getProducer(), map);
                                            else follows.add(t);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        /**
         * 1 - recorrer tot el file buscant les possibles produccions que poden donar el no termial que li estem buscant el follow
         * 2.1 - Si darrere té un terminal aquell serà un dels follows
         * 2.2 - Si darrere no té res, tindrà com a part del follow el follow del no terminal que ho ha produit
         * 2.3 - Si darrere té un no terminal, el follow que busquem tindrà el first del no terminal següent
         *       - Si a més, el first té el simbol epsilon, haurem d'afegir el follow del no terminal que ho ha produit en el follow que estem buscan
         */
    }

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

}
