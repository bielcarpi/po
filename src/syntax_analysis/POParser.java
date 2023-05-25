package syntax_analysis;

import entities.*;
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
            ErrorManager.getInstance().addError(new entities.Error(ErrorType.TOKEN_STREAM_ERROR, ErrorType.getMessage(ErrorType.TOKEN_STREAM_ERROR)));
            return null;
        }

        ArrayList<Production> productions = GrammarFactory.getGrammar(grammarFilePath);
        if(productions == null){
            ErrorManager.getInstance().addError(new entities.Error(ErrorType.TOKEN_STREAM_ERROR, ErrorType.getMessage(ErrorType.TOKEN_STREAM_ERROR)));
            return null;
        }


        ParsingTable pt = new ParsingTable();
        FirstFollowAnalyzer.fillParsingTable(pt, productions);


        //Now that we have built the ParsingTable and have the TokenStream, we can start the syntax analysis
        Stack<Object> stack = new Stack<>();
        stack.push(TokenType.EOF); //Add EOF & Axiom to the stack
        stack.push(productions.get(0).getProducer());
        ParseTree tree = new ParseTree(new ParseTreeNode(null, productions.get(0).getProducer(), new ArrayList<>()));
        ParseTreeNode currentNode = tree.getRoot();

        Production latestProduction = null;
        boolean errorDetected = false;
        boolean currentNodeChanged = true;
        outerLoop:
        while(!stack.empty()){
            //Si al stack hi tenim un no terminal
            if(stack.peek() instanceof String){
                String production = (String) stack.pop();
                while(true){ //Update currentNodeChanged
                    for(ParseTreeNode child: currentNode.getChildren()){
                        if(child.getSelf().equals(production) && child.getChildren().isEmpty()){
                            currentNode = child;
                            currentNodeChanged = true;
                            break;
                        }
                    }

                    if(!currentNodeChanged) currentNode = currentNode.getParent();
                    else break;
                }
                currentNodeChanged = false;

                ParsingTableValue ptv = pt.getProduction(new ParsingTableKey(production, ts.peekToken().getType()));
                if(ptv == null){    // ERROR
                    if(!errorDetected){
                        String err = "Error. No hem trobat fila i columna per " + production + " amb " + ts.peekToken().getType();
                        ErrorManager.getInstance().addError(new entities.Error(ErrorType.SYNTAX_ERROR, err, ts.peekToken().getLine(), ts.peekToken().getColumn()));
                    }

                    //Ens recuperem de l'error fent skip fins a trobar el seguent follow
                    if(latestProduction == null) break;
                    errorDetected = true;
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
                    currentNode.addChild(new ParseTreeNode(currentNode, o, o instanceof TokenType? null: new ArrayList<>()));
                    if(o == TokenType.VOID) continue;
                    stack.push(o);
                }
                Collections.reverse(currentNode.getChildren());
            }
            else{ //Si la stack hi tenim un terminal
                TokenType stackTerminal = (TokenType) stack.pop();
                Token input = ts.nextToken();
                if(stackTerminal.equals(input.getType())) {
                    for (ParseTreeNode ptn : currentNode.getChildren()) {
                        if (ptn.getSelf() instanceof TokenType && input.getType().equals(ptn.getSelf())) {
                            ptn.setToken(input); //MATCH
                        }
                    }
                    errorDetected = false; //If there was an error, it ends now
                }
                else {    // ERROR
                    if(!errorDetected){
                        String err = "Error. Esperavem " + stackTerminal + " i hem trobat " + input.getType();
                        ErrorManager.getInstance().addError(new entities.Error(ErrorType.SYNTAX_ERROR, err, input.getLine(), input.getColumn()));
                    }
                    //Ens recuperem de l'error fent skip fins a trobar un dels follows
                    if(latestProduction == null) break;
                    errorDetected = true;
                    List<TokenType> follows = latestProduction.getFollows();
                    while(!stack.isEmpty()){
                        if(follows.contains(ts.peekToken().getType())) break;
                        ts.nextToken();
                        if(ts.isEmpty()) break outerLoop;
                    }
                }
            }
        }

        ParseTree.cleanTree(tree);
        System.out.println(tree);
        ParseTree.runTACOptimization(tree);
        System.out.println(tree);
        try {
            semanticAnalyzer.validateParseTree(tree);
        } catch (RuntimeException ignored) {}
        ErrorManager.getInstance().printErrors();
        return tree;
    }
}
