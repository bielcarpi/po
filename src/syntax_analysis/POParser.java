package syntax_analysis;

import entities.ParseTree;
import entities.TokenStream;
import entities.TokenType;
import lexical_analysis.Lexer;
import org.jetbrains.annotations.NotNull;
import semantic_analysis.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

        ts.printStream();

        //Now that we have built the ParsingTable and have the TokenStream, we can start the syntax analysis
        Stack<Object> stack = new Stack<>();
        stack.push(TokenType.EOF); //Add EOF & Axiom to the stack
        stack.push(productions.get(0).getProducer());

        Production latestProduction = null;
        outerLoop:
        while(!stack.empty()){
            //Si al stack hi tenim un no terminal
            if(stack.peek() instanceof String){
                String production = (String) stack.pop();
                ParsingTableValue ptv = pt.getProduction(new ParsingTableKey(production, ts.peekToken().getType()));
                if(ptv == null){    // ERROR
                    System.out.println("Error. No hem trobat fila i columna per " + production + " amb " + ts.peekToken().getType());
                    //Ens recuperem de l'error fent skip fins a trobar el seguent follow
                    if(latestProduction == null) break;
                    List<TokenType> follows = pt.getProduction(production).getFollows();
                    while(!stack.isEmpty()){
                        if(follows.contains(ts.peekToken().getType())) break;
                        ts.nextToken();
                        if(ts.isEmpty()) break outerLoop;
                    }

                    continue;
                }

                latestProduction = ptv.production();
                ArrayList<Object> derivation = ptv.production().getDerived().get(ptv.derivation());
                ArrayList<Object> derivationCopy = new ArrayList<>(derivation);
                Collections.reverse(derivationCopy);
                for(Object o: derivationCopy){
                    if(o == TokenType.VOID) continue;
                    stack.push(o);
                }
            }
            else{ //Si la stack hi tenim un terminal
                TokenType stackTerminal = (TokenType) stack.pop();
                TokenType inputTerminal = ts.nextToken().getType();
                if(stackTerminal.equals(inputTerminal)) {   // MATCH
                    System.out.println("Match de " + stackTerminal + " amb " + inputTerminal);
                }
                else {    // ERROR
                    System.out.println("Error. Esperavem " + stackTerminal + " i hem trobat " + inputTerminal);
                    //Ens recuperem de l'error fent skip fins a trobar un dels follows
                    if(latestProduction == null) break;
                    List<TokenType> follows = latestProduction.getFollows();
                    while(!stack.isEmpty()){
                        if(follows.contains(ts.peekToken().getType())) break;
                        ts.nextToken();
                        if(ts.isEmpty()) break outerLoop;
                    }
                }
            }
        }

        return new ParseTree();
    }
}
