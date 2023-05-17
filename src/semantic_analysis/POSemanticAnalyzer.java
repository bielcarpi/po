package semantic_analysis;

import entities.*;
import entities.Error;
import org.jetbrains.annotations.NotNull;

public class POSemanticAnalyzer implements SemanticAnalyzer {
    @Override
    public void validateParseTree(@NotNull ParseTree pt) {
        exploreTACOTree(pt.getRoot(), "global");
    }

    private void exploreTACOTree(@NotNull ParseTreeNode root, String scope){
        for(ParseTreeNode ptn: root.getChildren()){
            if (ptn.getSelf().toString().equals("assignacio")) {
                SymbolTableEntry ste = SymbolTable.getInstance().lookup(ptn.getChildren().get(0).getToken().getData(), scope);
                if (ste == null) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.VARIABLE_UNDECLARED,
                            "Error, undeclared variable: " + ptn.getChildren().get(0).getToken().getData(),
                            ptn.getChildren().get(0).getToken().getLine(), ptn.getChildren().get(0).getToken().getColumn()));
                    continue;
                }

                if (ptn.getChildren().size() == 2) { // Assign type int
                    if (ste.getType() == TokenType.UNKNOWN) {
                        ste.setType(TokenType.INT);
                    } else if (ste.getType() != TokenType.INT) {
                        ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                                "Error, type mismatch. Trying to assign type INT to variable " + ste.getId() + " of type " + ste.getType(),
                                ptn.getChildren().get(0).getToken().getLine(), ptn.getChildren().get(0).getToken().getColumn()));
                    }
                    continue;
                }

                TokenType type = validateAssignacio(ptn.getChildren().get(2), scope);
                if (type == TokenType.UNKNOWN) {
                    continue;
                }

                 if (ste.getType() == TokenType.UNKNOWN) { // Variable has not been set before
                     ste.setType(type); // Assign type and move on
                 } else {
                        if (ste.getType() != type) { // If type already exists and is different, throw error
                            ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                                    "Error, type mismatch. Trying to assign type " + type + " to variable '" + ste.getId() + "' of type " + ste.getType(),
                                    ptn.getChildren().get(0).getToken().getLine(), ptn.getChildren().get(0).getToken().getColumn()));
                        }
                        // Else, type is the same, so we can move on
                 }
            } else if (ptn.getSelf().toString().equals("ID")
                    || ptn.getSelf().toString().equals("MAIN")) {
                if (ptn.getChildren() != null) {
                    exploreTACOTree(ptn, ptn.getToken().getData());
                }
            }
        }

    }

    private TokenType validateAssignacio(@NotNull ParseTreeNode node, String scope) {
        if (!node.getSelf().equals("exp")) { // If not exp, then it can only be ID,INT or STRING
            if (node.getSelf().toString().equals("ID")) {
                SymbolTableEntry ste = SymbolTable.getInstance().lookup(node.getToken().getData(), scope);
                if (ste == null) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.VARIABLE_UNDECLARED,
                            "Error, undeclared variable: " + node.getToken().getData(),
                            node.getToken().getLine(), node.getToken().getColumn()));
                    return TokenType.UNKNOWN;
                }
                return ste.getType();
            } else if (node.getSelf().toString().equals("INT")) {
                return TokenType.INT;
            } else if (node.getSelf().toString().equals("STRING")) {
                return TokenType.STRING;
            }
        }

        TokenType firstChildType;
        TokenType thirdChildType;
        ParseTreeNode firstChild = node.getChildren().get(0);
        ParseTreeNode secondChild = node.getChildren().get(1);
        ParseTreeNode thirdChild = node.getChildren().get(2);

        if (firstChild.toString().equals("exp")) {
            firstChildType = validateAssignacio(firstChild, scope);

            // If returned unknown, error has already been detected
            if (firstChildType == TokenType.UNKNOWN) return TokenType.UNKNOWN;
        } else if (firstChild.getSelf().toString().equals("ID")) {
            SymbolTableEntry steFirstChild = SymbolTable.getInstance().lookup(firstChild.getToken().getData(), scope);
            if (steFirstChild == null) {
                ErrorManager.getInstance().addError(new entities.Error(ErrorType.VARIABLE_UNDECLARED,
                        "Error, undeclared variable: " + firstChild.getToken().getData(),
                        firstChild.getToken().getLine(), firstChild.getToken().getColumn()));
                return TokenType.UNKNOWN;
            }
            firstChildType = steFirstChild.getType();
        } else {
            firstChildType = firstChild.getToken().getType();
        }


        if (thirdChild.toString().equals("exp")) {
            thirdChildType = validateAssignacio(thirdChild, scope);

            // If returned unknown, error has already been detected
            if (thirdChildType == TokenType.UNKNOWN) return TokenType.UNKNOWN;
        } else if (thirdChild.getSelf().toString().equals("ID")) {
            SymbolTableEntry steThirdChild = SymbolTable.getInstance().lookup(thirdChild.getToken().getData(), scope);
            if (steThirdChild == null) {
                ErrorManager.getInstance().addError(new entities.Error(ErrorType.VARIABLE_UNDECLARED,
                        "Error, undeclared variable: " + thirdChild.getToken().getData(),
                        thirdChild.getToken().getLine(), thirdChild.getToken().getColumn()));
                return TokenType.UNKNOWN;
            }
            thirdChildType = steThirdChild.getType();
        } else {
            thirdChildType = thirdChild.getToken().getType();
        }

        if (firstChildType == thirdChildType) return thirdChildType;


        // Error if types don't match
        ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                "Error, there was a mismatch operation type. Trying to " + secondChild.getToken().getType() +
                        " a " + firstChildType + " with " + thirdChildType,
                thirdChild.getToken().getLine(), thirdChild.getToken().getColumn()));

        return TokenType.UNKNOWN;
    }
}
