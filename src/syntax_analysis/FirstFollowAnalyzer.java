package syntax_analysis;

import entities.TokenType;

import java.util.ArrayList;
import java.util.List;

public class FirstFollowAnalyzer {

    /**
     * Fills the provided Parsing Table, given the List of Productions
     * @param pt The ParsingTable to fill
     * @param productions The productions to analyze
     * @return Whether grammar is ambiguous or not
     */
    public static boolean fillParsingTable(ParsingTable pt, List<Production> productions){


        productions.get(0).setFirsts();
        productions.get(0).setFollows();

        //Example
        pt.addProduction(new ParsingTableTuple("E", TokenType.OPEN_PAREN), productions.get(0));

        return true;
    }
}
