import entities.ErrorManager;
import entities.ParseTree;
import entities.TAC;
import intermediate_code_generator.POTACGenerator;
import intermediate_code_generator.TACGenerator;
import intermediate_code_optimizer.POTACOptimizer;
import lexical_analysis.POLexer;
import mips_generator.POMIPSGenerator;
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

        Parser parser = new POParser(pureHLL);
        ParseTree pt = parser.generateParseTree(new POLexer(), new POSemanticAnalyzer());

        //If the frontend has errors, the backend will not be executed
        if(ErrorManager.getInstance().hasErrors()){
            ErrorManager.getInstance().printErrors();
            return;
        }

        TACGenerator tacGenerator = new POTACGenerator(new POTACOptimizer(), true);
        TAC tac = tacGenerator.generateTAC(pt);

        POMIPSGenerator mipsGenerator = new POMIPSGenerator();
        mipsGenerator.generateMIPS(tac, file.split("\\.")[0].concat(".asm"));
    }
}