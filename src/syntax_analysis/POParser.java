package syntax_analysis;

import entities.ParseTree;
import entities.TokenStream;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import semantic_analysis.SemanticAnalyzer;

import java.util.ArrayList;

public class POParser implements Parser {

    private final String pureHLL;
    private final String grammarFilePath = "grammar.txt";

    public POParser(@NotNull final String pureHLL) {
        this.pureHLL = pureHLL;
    }

    @Override
    public @NotNull ParseTree generateParseTree(@NotNull Lexer lexer, @NotNull SemanticAnalyzer semanticAnalyzer) {
        TokenStream ts = lexer.generateTokenStream(pureHLL);
        if(ts == null){
            //TODO: Manage error
            System.out.println("Error generating the token stream");
            return new ParseTree();
        }

        ArrayList<Production> productions = new GrammarFactory().getGrammar(grammarFilePath);
        if(productions == null){
            //TODO: Manage error
            System.out.println("Error parsing the grammar");
            return new ParseTree();
        }

        for(Production p: productions) System.out.println(p);

        return new ParseTree();
    }
}
