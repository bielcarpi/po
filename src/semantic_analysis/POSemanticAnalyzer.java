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
                TokenType type = validateAssignacio(ptn, scope);
                if (type == TokenType.UNKNOWN) { // An error has already been detected in this assignment so we can ignore
                    continue;
                }

                // Assign type to first child
                 SymbolTableEntry ste = SymbolTable.getInstance().lookup(ptn.getChildren().get(0).getToken().getData(), scope);

                 if (ste == null) {
                     ErrorManager.getInstance().addError(new entities.Error(ErrorType.VARIABLE_UNDECLARED,
                             "Error, undeclared variable: " + ptn.getChildren().get(0).getToken().getData(),
                             ptn.getChildren().get(0).getToken().getLine(), ptn.getChildren().get(0).getToken().getColumn()));
                     continue;
                 }

                 if (ste.getType() == TokenType.UNKNOWN) { // Variable has not been set before
                     ste.setType(type); // Assign type and move on
                 } else {
                        if (ste.getType() != type) { // If type already exists and is different, throw error
                            ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                                    "Error, type mismatch. Trying to assign type " + type + " to variable " + ste.getId() + " of type " + ste.getType(),
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
        // 1rst case: The node has 2 children
        // If the node has 2 children, it means that it is a ++ or -- operation (which means type is INT)
        if (node.getChildren().size() == 2) return TokenType.INT;

        TokenType firstChildType;
        TokenType thirdChildType;
        ParseTreeNode firstChild = node.getChildren().get(0);
        ParseTreeNode secondChild = node.getChildren().get(1);
        ParseTreeNode thirdChild = node.getChildren().get(2);

        if (thirdChild.toString().equals("exp")) {
            thirdChildType = validateAssignacio(thirdChild, scope);
        } else {
            thirdChildType = thirdChild.getSelf().toString().equals("ID") ?
                    SymbolTable.getInstance().lookup(thirdChild.getToken().getData(), scope).getType() :
                    thirdChild.getToken().getType();
        }

        // If type UNKNOWN is reached here, it means that previous types have failed so we can ignore and return
        if (thirdChildType == TokenType.UNKNOWN) return TokenType.UNKNOWN;

        // Check types match
        // We have to check the value of the second child and if it is not and EQU, we have
        // to check the type of the first child
        if (firstChild.getSelf().toString().equals("ID") &&
            !secondChild.getSelf().toString().equals("EQU")) {
            firstChildType = SymbolTable.getInstance().lookup(firstChild.getToken().getData(), scope).getType();
        } else if (firstChild.getSelf().toString().equals("ID")) {
            return thirdChildType;
        } else {
            firstChildType = firstChild.getToken().getType();
        }
        if (firstChildType == thirdChildType) return thirdChildType;

        // Error if types don't match
        ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                "Error, there was a mismatch operation type. Trying to " + node.getChildren().get(1).getToken().getType() +
                        " a " + firstChild.getToken().getType() + " with " + thirdChild.getToken().getType(),
                thirdChild.getToken().getLine(), thirdChild.getToken().getColumn()));

        return TokenType.UNKNOWN;
    }
}
