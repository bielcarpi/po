package testing;

import entities.ErrorManager;
import entities.ParseTree;
import entities.TAC;
import intermediate_code_generator.POTACGenerator;
import intermediate_code_generator.TACGenerator;
import intermediate_code_optimizer.POTACOptimizer;
import lexical_analysis.POLexer;
import mips_generator.POMIPSGenerator;
import org.junit.jupiter.api.Test;
import preprocessor.POPreprocessor;
import semantic_analysis.POSemanticAnalyzer;
import syntax_analysis.POParser;
import syntax_analysis.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class POMipsTests {

    private void test(String input, String output) {
        try {
            String pureHLL = new POPreprocessor().generatePureHighLevelLanguage(input);
            Parser parser = new POParser(pureHLL);
            ParseTree pt = parser.generateParseTree(new POLexer(), new POSemanticAnalyzer());

            if(ErrorManager.getInstance().hasErrors()) return;


            TACGenerator tacGenerator = new POTACGenerator(new POTACOptimizer(), true);
            TAC tac = tacGenerator.generateTAC(pt);

            POMIPSGenerator mipsGenerator = new POMIPSGenerator(input.split("\\.")[0].concat(".asm"));
            mipsGenerator.generateMIPS(tac);
            String expected = Files.readString(Path.of(output));
            String actual = Files.readString(Path.of(input.split("\\.")[0].concat(".asm")));
            assertEquals(expected, actual);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void integration_sortElse() {
        test("src/testing/integration/po_mips/inputs/sort-else.po", "src/testing/integration/po_mips/outputs/sort-else.po");
    }

    /*
    @Test
    void integration_sortSw() {
        test("src/testing/integration/po_mips/inputs/sort-sw.po", "src/testing/integration/po_mips/outputs/sort-sw.po");
    }

    @Test
    void integration_recursiveFactorial() {
        test("src/testing/integration/po_mips/inputs/recursive-factorial.po", "src/testing/integration/po_mips/outputs/recursive-factorial.po");
    }

    @Test
    void integration_operations() {
        test("src/testing/integration/po_mips/inputs/operations.po", "src/testing/integration/po_mips/outputs/operations.po");
    }*/


    @Test
    void unit_ifElse() {
        test("src/testing/unit/po_mips/inputs/if-else.po", "src/testing/unit/po_mips/outputs/if-else.asm");
    }

    @Test
    void unit_switch() {
        test("src/testing/unit/po_mips/inputs/switch.po", "src/testing/unit/po_mips/outputs/switch.asm");
    }

    @Test
    void unit_for() {
            test("src/testing/unit/po_mips/inputs/for.po", "src/testing/unit/po_mips/outputs/for.asm");
    }

    @Test
    void unit_while() {
        test("src/testing/unit/po_mips/inputs/while.po", "src/testing/unit/po_mips/outputs/while.asm");
    }

    @Test
    void unit_vars() {
        test("src/testing/unit/po_mips/inputs/vars.po", "src/testing/unit/po_mips/outputs/vars.asm");
    }

    @Test
    void unit_functions() {
        test("src/testing/unit/po_mips/inputs/func.po", "src/testing/unit/po_mips/outputs/func.asm");
    }

    @Test
    void unit_helloWorld() {
        test("src/testing/unit/po_mips/inputs/hello-world.po", "src/testing/unit/po_mips/outputs/hello-world.asm");
    }
}