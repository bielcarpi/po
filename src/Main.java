import entities.ParseTree;
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
        new POLexer().generateTokenStream("func main(){\n\tvar a = 3\n}");


        //Parser parser = new POParser(pureHLL);
        //ParseTree pt = parser.generateParseTree(new POLexer(), new POSemanticAnalyzer());

        //System.out.println(pt);
    }
}