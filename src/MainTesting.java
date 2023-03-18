import entities.AbstractSyntaxTree;
import preprocessor.Preprocessor;
import syntax_analysis.Parser;

public class MainTesting {

    private final static String file = "file.po";
    public static void main(String[] args) {

        Preprocessor preprocessor = new POPreprocessor();
        String pureHLL = preprocessor.generatePureHighLevelLanguage(file);

        Parser parser = new POParser(pureHLL);
        AbstractSyntaxTree ast = parser.generateAbstractSyntaxTree(new POLexer(), new POSemanticAnalyzer());

        AbstractSyntaxTree.printTree(ast);
    }
}