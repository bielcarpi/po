package semantic_analysis;

import entities.*;
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
            } else if (ptn.getSelf().toString().equals("exp")) {
                // Top level exp can only be found in conditionals and loops, there for second child must be a comparison operator
                if (ptn.getChildren().size() == 2) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid expression",
                            ptn.getChildren().get(1).getToken().getLine(), ptn.getChildren().get(1).getToken().getColumn()));
                    continue;
                }

                if (ptn.getChildren().get(1).getToken().getType() != TokenType.DIFF &&
                        ptn.getChildren().get(1).getToken().getType() != TokenType.DOUBLE_EQU &&
                        ptn.getChildren().get(1).getToken().getType() != TokenType.GT &&
                        ptn.getChildren().get(1).getToken().getType() != TokenType.GTE &&
                        ptn.getChildren().get(1).getToken().getType() != TokenType.LT &&
                        ptn.getChildren().get(1).getToken().getType() != TokenType.LTE) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid expression. Expression must contain a comparison operator",
                            ptn.getChildren().get(1).getToken().getLine(), ptn.getChildren().get(1).getToken().getColumn()));
                    continue;
                }
                TokenType type = validateAssignacio(ptn, scope);
                if (type == TokenType.STRING) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid expression. Trying to compare strings",
                            ptn.getChildren().get(1).getToken().getLine(), ptn.getChildren().get(1).getToken().getColumn()));
                }
            } else if ((ptn.getSelf().toString().equals("ID") && ptn.getParent().getSelf().equals("<programa>"))
                    || ptn.getSelf().toString().equals("MAIN")) {
                if (ptn.getChildren() != null) {
                    exploreTACOTree(ptn, ptn.getToken().getData());
                }
            } else if (ptn.getSelf().toString().equals("FOR")) {
                if (!ptn.getChildren().get(1).getSelf().equals("exp")) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid expression. Cannot use value as condition",
                            ptn.getChildren().get(1).getToken().getLine(), ptn.getChildren().get(1).getToken().getColumn()));
                    continue;
                }
                if (ptn.getChildren() != null) {
                    exploreTACOTree(ptn, scope);
                }
            } else if (ptn.getSelf().toString().equals("IF") || ptn.getSelf().toString().equals("ELSIF") ||
                        ptn.getSelf().toString().equals("WHILE")) {
                // If or elsif, as well as while, will have exp as a first child, and this exp must be a comparison
                if (!ptn.getChildren().get(0).getSelf().equals("exp")) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid expression. Cannot use value as condition",
                            ptn.getChildren().get(0).getToken().getLine(), ptn.getChildren().get(0).getToken().getColumn()));
                    continue;
                }
                if (ptn.getChildren() != null) {
                    exploreTACOTree(ptn, scope);
                }
            } else if (ptn.getSelf() == TokenType.ID){ // Function call without assignment
                SymbolTableFunctionEntry ste = (SymbolTableFunctionEntry) SymbolTable.getInstance().lookup(ptn.getToken().getData(), scope);
                if (ste == null) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.FUNCTION_UNDECLARED,
                            "Error, undeclared function: " + ptn.getToken().getData(),
                            ptn.getToken().getLine(), ptn.getToken().getColumn()));
                    continue;
                }
                if (ste.getArguments() != ptn.getChildren().size()) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid number of arguments for function: " + ptn.getToken().getData(),
                            ptn.getToken().getLine(), ptn.getToken().getColumn()));
                    continue;
                }
                for (ParseTreeNode node : ptn.getChildren()) {
                    if (node.getSelf().equals("exp")) {
                        TokenType type = validateAssignacio(node, scope);
                        if (type != TokenType.INT) {
                            ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                                    "Error, invalid argument type for function: " + ptn.getToken().getData(),
                                    ptn.getToken().getLine(), ptn.getToken().getColumn()));
                        }
                    } else {
                        if (node.getSelf() != TokenType.INT) {
                            ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                                    "Error, invalid argument type for function: " + ptn.getToken().getData(),
                                    ptn.getToken().getLine(), ptn.getToken().getColumn()));
                        }
                    }
                }
            } else {
                if (ptn.getChildren() != null) {
                    exploreTACOTree(ptn, scope);
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
                            "Error, undeclared identifier: " + node.getToken().getData(),
                            node.getToken().getLine(), node.getToken().getColumn()));
                    return TokenType.UNKNOWN;
                }
                if (node.getChildren() != null) {
                    // It's a function call
                    if (validateFunctionCall(node,
                            (SymbolTableFunctionEntry) SymbolTable.getInstance().lookup(node.getToken().getData(), scope),
                            scope)) {
                        return TokenType.UNKNOWN;
                    }
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
                        "Error, undeclared identifier: " + firstChild.getToken().getData(),
                        firstChild.getToken().getLine(), firstChild.getToken().getColumn()));
                return TokenType.UNKNOWN;
            }

            if (firstChild.getChildren() != null) {
                // It's a function call
                if (validateFunctionCall(firstChild,
                        (SymbolTableFunctionEntry) SymbolTable.getInstance().lookup(firstChild.getToken().getData(), scope),
                        scope)) {
                    return TokenType.UNKNOWN;
                }
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
                        "Error, undeclared identifier: " + thirdChild.getToken().getData(),
                        thirdChild.getToken().getLine(), thirdChild.getToken().getColumn()));
                return TokenType.UNKNOWN;
            }

            if (thirdChild.getChildren() != null) {
                // It's a function call
                if (validateFunctionCall(thirdChild,
                        (SymbolTableFunctionEntry) SymbolTable.getInstance().lookup(thirdChild.getToken().getData(), scope),
                        scope)) {
                    return TokenType.UNKNOWN;
                }
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

    private boolean validateFunctionCall(ParseTreeNode parent, SymbolTableFunctionEntry ste, String scope) {
        if (ste == null) {
            ErrorManager.getInstance().addError(new entities.Error(ErrorType.FUNCTION_UNDECLARED,
                    "Error, undeclared function: " + parent.getToken().getData(),
                    parent.getToken().getLine(), parent.getToken().getColumn()));
            return true;
        }
        if (ste.getArguments() != parent.getChildren().size()) {
            ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                    "Error, invalid number of arguments for function: " + parent.getToken().getData(),
                    parent.getToken().getLine(), parent.getToken().getColumn()));
            return true;
        }
        for (ParseTreeNode node : parent.getChildren()) {
            if (node.getSelf().equals("exp")) {
                TokenType type = validateAssignacio(node, scope);
                if (type != TokenType.INT) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid argument type for function: " + parent.getToken().getData(),
                            parent.getToken().getLine(), parent.getToken().getColumn()));
                    return true;
                }
            } else {
                if (node.getSelf() != TokenType.INT) {
                    ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                            "Error, invalid argument type for function: " + parent.getToken().getData(),
                            parent.getToken().getLine(), parent.getToken().getColumn()));
                    return true;
                }
            }
        }

        return false;
    }
}
