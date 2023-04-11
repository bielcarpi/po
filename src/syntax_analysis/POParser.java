package syntax_analysis;

import entities.ParseTree;
import entities.TokenStream;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import semantic_analysis.SemanticAnalyzer;

import java.util.ArrayList;

public class POParser implements Parser {

    private final String pureHLL;
    private final String grammarFilePath = "grammarEasy.txt";

    public POParser(@NotNull final String pureHLL) {
        this.pureHLL = pureHLL;
    }

    @Override
    public @NotNull ParseTree generateParseTree(@NotNull Lexer lexer, @NotNull SemanticAnalyzer semanticAnalyzer) {
        TokenStream ts = lexer.generateTokenStream(pureHLL);
        if(ts == null){
            //TODO: Manage critical error
            System.out.println("Error generating the token stream");
            return new ParseTree();
        }

        ArrayList<Production> productions = GrammarFactory.getGrammar(grammarFilePath);
        if(productions == null){
            //TODO: Manage critical error
            System.out.println("Error parsing the grammar");
            return new ParseTree();
        }


        ParsingTable pt = new ParsingTable();
        FirstFollowAnalyzer.fillParsingTable(pt, productions);
        for(Production p: productions) System.out.println(p);
        System.out.println(pt);

        //Now that we have built the ParsingTable and have the TokenStream, we can start the syntax analysis

        return new ParseTree();
    }
}
