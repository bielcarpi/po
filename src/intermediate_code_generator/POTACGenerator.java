package intermediate_code_generator;

import entities.*;
import intermediate_code_optimizer.TACOptimizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class that generates TAC from a parse tree
 *
 * @see TAC
 * @see TACOptimizer
 * @see ParseTree
 */
public class POTACGenerator implements TACGenerator{

    private final TACOptimizer tacOptimizer;
    private final boolean outputFile;
    private static final String WORK_REG = "t9";
    private TACBlock tacBlock; //Current block aux for the traverseTree method

    /**
     * Default constructor
     * @param tacOptimizer the optimizer to be used
     * @param outputFile if true, the TAC generated in {@link #generateTAC(ParseTree)} will be written to a file called "output.tac"
     */
    public POTACGenerator(@Nullable TACOptimizer tacOptimizer, boolean outputFile){
        this.tacOptimizer = tacOptimizer;
        this.outputFile = outputFile;
    }


    @Override
    public @NotNull TAC generateTAC(@NotNull ParseTree pt) {
        TAC tac = new TAC(); //Data structure to store the TAC

        //If the root is MAIN, we only have to traverse the main function
        if(pt.getRoot().getToken() != null){
            tacBlock = new TACBlock(false);
            tac.add(pt.getRoot().getToken().getData(), tacBlock);
            traverseTree(pt.getRoot(), tac, pt.getRoot().getToken().getData()); //Traverse the function and generate TAC inside the block
        }
        else {
            //New block for the global assignations
            tacBlock = new TACBlock(false);
            tac.add("global", tacBlock);

            //Loop through the children of the root (either assignations or functions)
            for (ParseTreeNode child : pt.getRoot().getChildren()) {
                if (child.getSelf().equals("assignacio")) {
                    generateTACAssignacio(child);
                } else { //Main or normal function
                    //New block for the function
                    tacBlock = new TACBlock(false);
                    tac.add(child.getToken().getData(), tacBlock);
                    traverseTree(child, tac, child.getToken().getData()); //Traverse the function and generate TAC inside the block
                }
            }
        }

        if(tacOptimizer != null)
            tac = tacOptimizer.optimizeTAC(tac);

        if(outputFile)
            writeToFile(tac.toString());

        return tac;
    }

    private void traverseTree(@NotNull ParseTreeNode node, @NotNull TAC tac, String scope){

        if(node.getToken() != null && node.getToken().getType() == TokenType.IF){
            generateTACIf(node, tac, scope);
            return;
        }
        else if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node);
            return;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return;
        for(ParseTreeNode child: node.getChildren())
            traverseTree(child, tac, scope);
    }

    /**
     * Generates TAC for an if statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private void generateTACIf(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        //We need two new blocks, one if the condition is true and another one if it is false (to jump to the end of the if)
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, trueBlock);
        tac.add(scope, falseBlock);

        //TODO: Add condition
        //TODO: IF condition variable should be declared in the scope of that function (it is a valid variable in the scope)
        trueBlock.add(new TACEntry("a", "7", falseBlock.getBlockNum(), TACType.IFGEQ));

        //Traverse the true block & add the entries to the true block
        tacBlock = trueBlock; //Update current block working on
        traverseTree(node.getChildren().get(1), tac, scope);

        tacBlock = falseBlock; //Update current block working on

        //Traverse all else if & else
        for(int i = 2; i < node.getChildren().size(); i++){
            generateTACElsif(node.getChildren().get(i), tac, scope);
        }


    }

    private void generateTACElsif(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        //We just need another new block (to jump if the condition is false)

        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, falseBlock);

        //TODO: Add condition (node.GetChildren().get(0))
        //If it is an ELSIF
        if(node.getToken().getType() == TokenType.ELSIF){
            tacBlock.add(new TACEntry("a", "7", falseBlock.getBlockNum(), TACType.IFGEQ));
            traverseTree(node.getChildren().get(1), tac, scope);
        }
        else{ //IF it is an ELSE
            traverseTree(node, tac, scope);
        }

        //Update current block working on
        tacBlock = falseBlock;
    }


    /**
     * Generates TAC for an assignation
     * @param node the node to be traversed
     */
    private void generateTACAssignacio(@NotNull ParseTreeNode node){
        TACEntry entry;
        if(node.getChildren().size() == 2){ //Special case: ++ or --
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(0).getToken().getData(),
                    "1",
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){ //x = z OP exp
            traverseTACAssignacio(node.getChildren().get(2));

            //All the temporary values calculated by the traversal are stored in WORK_REG
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    WORK_REG,
                    TACType.EQU);
        }
        else{ //x = z
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }

        //Add the entry to the block
        tacBlock.add(entry);
    }

    /**
     * Traverses the tree and generates TAC for the assignations
     * @param node the node to be traversed
     * @param tacBlock the block where the TAC will be stored
     */
    private void traverseTACAssignacio(@NotNull ParseTreeNode node){
        TACEntry entry;

        //If the first child is equ, go down the tree
        if(node.getChildren().get(0).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(0));

            //t = (t-1) OP ch2
            entry = new TACEntry(WORK_REG, WORK_REG, node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2));

            //t = ch0 OP (t-1)
            entry = new TACEntry(WORK_REG, node.getChildren().get(0).getToken().getData(), WORK_REG,
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else{
            //t = ch0 OP ch2
            entry = new TACEntry(WORK_REG, node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }

        tacBlock.add(entry);
    }


    /**
     * Writes the given TAC to a file called "output.tac"
     * @param tac the TAC to be written
     */
    private void writeToFile(@NotNull String tac){
        try {
            PrintWriter out = new PrintWriter("output.tac");
            out.println(tac);
            out.close();
        } catch (IOException e) {
            ErrorManager.getInstance().addError(new entities.Error(ErrorType.TAC_GENERATION_ERROR, "Error while writing to file"));
        }
    }
}
