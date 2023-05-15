package semantic_analysis;

import entities.*;
import org.jetbrains.annotations.NotNull;

public class POSemanticAnalyzer implements SemanticAnalyzer {
    @Override
    public void validateParseTree(@NotNull ParseTree ast) {



    }

    private void exploreTACOTree(@NotNull ParseTreeNode root, String scope){
        for(ParseTreeNode ptn: root.getChildren()){
            if (ptn.getSelf().toString().equals("assignacio")) {
                TokenType type = validateAssignacio(ptn);
                // Assign type to first child
                 SymbolTableEntry ste = SymbolTable.getInstance().lookup(root.getChildren().get(0).getToken().getData() + scope);

                 ste.setType(type);
            } else if (ptn.getSelf().toString().equals("ID")) {

            } else if (ptn.getSelf().toString().equals("MAIN")) {

            }
        }

    }

    private TokenType validateAssignacio(@NotNull ParseTreeNode node) {
        // 1rst case: The node has 2 children
        // If the node has 2 children, it means that it is a ++ or -- operation (which means type is INT)
        if (node.getChildren().size() == 2) return TokenType.INT;

        TokenType opType;

        ParseTreeNode firstChild = node.getChildren().get(0);
        ParseTreeNode thirdChild = node.getChildren().get(2);
        if (thirdChild.toString().equals("exp")) {
            opType = validateAssignacio(thirdChild);
        } else {
            opType = thirdChild.getToken().getType();
        }

        // If type UNKNOWN is reached here, it means that previous types have failed so we can ignore and return
        if (opType == TokenType.UNKNOWN) return TokenType.UNKNOWN;

        // Check types match
        if (opType == firstChild.getToken().getType()) return opType;

        // Error if types don't match
        ErrorManager.getInstance().addError(new entities.Error(ErrorType.MISMATCHED_TYPE_OPERATION,
                "Error, there was a mismatch operation type. Trying to match " + firstChild.getToken().getType() + " with " +
                thirdChild.getToken().getType(),
                node.getToken().getLine(), node.getToken().getColumn()));

        return TokenType.UNKNOWN;
    }
}
