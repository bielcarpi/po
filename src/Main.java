import entities.ParseTree;
import entities.TokenStream;
import lexical_analysis.POLexer;
import preprocessor.POPreprocessor;
import preprocessor.Preprocessor;
import semantic_analysis.POSemanticAnalyzer;
import syntax_analysis.POParser;
import syntax_analysis.Parser;

public class Main {

    private final static String file = "file.po";
    public static void main(String[] args) {

        Preprocessor preprocessor = new POPreprocessor();
        String pureHLL = preprocessor.generatePureHighLevelLanguage(file);

        //Simulate the preprocessor is working and that parser calls the Lexer
        TokenStream ts
        = new POLexer().generateTokenStream("func main(){\n\tvar a = 3\n}");
        assert ts!= null;
        while (!ts.isEmpty()){
            System.out.println(ts.nextToken().toString());
        }

        //Parser parser = new POParser(pureHLL);
        //ParseTree pt = parser.generateParseTree(new POLexer(), new POSemanticAnalyzer());

        //System.out.println(pt);
    }
}