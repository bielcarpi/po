package intermediate_code_generator;

import entities.*;
import intermediate_code_optimizer.TACOptimizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
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

            //The program can have either assignations or functions as children
            //We'll first loop through the assignations and then through the functions
            for (ParseTreeNode child : pt.getRoot().getChildren()) {
                if (child.getSelf().equals("assignacio")) {
                    generateTACAssignacio(child);
                }
            }

            for (ParseTreeNode child : pt.getRoot().getChildren()){
                if (!child.getSelf().equals("assignacio")) {
                    tacBlock = new TACBlock(false);
                    tac.add(child.getToken().getData(), tacBlock);
                    traverseTree(child, tac, child.getToken().getData()); //Traverse the function and generate TAC inside the block

                    //Add a return at the end of the function if it doesn't have one
                    if(!tacBlock.getEntries().isEmpty() && tacBlock.getEntries().get(tacBlock.getEntries().size() - 1).getType() != TACType.RET)
                        tacBlock.add(new TACEntry(null, "0", TACType.RET));
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

        if(node.getToken() != null){
            switch (node.getToken().getType()) {
                case IF -> {
                    generateTACIf(node, tac, scope);
                    return;
                }
                case SWITCH -> {
                    generateTACSwitch(node, tac, scope);
                    return;
                }
                case FOR -> {
                    generateTACFor(node, tac, scope);
                    return;
                }
                case WHILE -> {
                    generateTACWhile(node, tac, scope);
                    return;
                }
                case RET -> {
                    tacBlock.add(new TACEntry(null, node.getChildren().get(0).getToken().getData(), TACType.RET));
                    return;
                }
                case BREAK -> { //Converted to GOTO
                    tacBlock.add(new TACEntry(tacBlock.getBlockNum()+1, TACType.GOTO));
                    return;
                }
                case CONTINUE -> { //Converted to GOTO
                    tacBlock.add(new TACEntry(tacBlock.getBlockNum(), TACType.GOTO));
                    return;
                }
            }
        }

        if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node);
            return;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return;
        for(ParseTreeNode child: node.getChildren())
            traverseTree(child, tac, scope);
    }

    /**
     * Generates TAC for a Switch statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private void generateTACSwitch(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        //We need a new block for each case, one for the default, and one for the end of the switch
        TACBlock endBlock = new TACBlock(true);

        //Add to current block all conditions and jumps to the case blocks
        for(int i = 1; i < node.getChildren().size() - 1; i++){
            tacBlock.add(new TACEntry(node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(i).getChildren().get(0).getToken().getData(),
                    endBlock.getBlockNum() + i, TACType.IFEQU));
        }
        //Add default jump, or skip switch
        if(node.getChildren().get(node.getChildren().size() - 1).getToken().getType() == TokenType.DEFAULT)
            tacBlock.add(new TACEntry(endBlock.getBlockNum() + node.getChildren().size()-1, TACType.GOTO));
        else
            tacBlock.add(new TACEntry(endBlock.getBlockNum(), TACType.GOTO));

        for(int i = 1; i < node.getChildren().size(); i++){
            TACBlock caseBlock = new TACBlock(true);
            tac.add(scope, caseBlock);
            tacBlock = caseBlock;

            if(node.getChildren().get(i).getToken().getType() == TokenType.DEFAULT){
                for(int j = 1; j < node.getChildren().get(i).getChildren().size(); j++)
                    traverseTree(node.getChildren().get(i).getChildren().get(j), tac, scope);
            }
            else{
                for(int j = 1; j < node.getChildren().get(i).getChildren().size(); j++)
                    traverseTree(node.getChildren().get(i).getChildren().get(j), tac, scope);

                //Finally, we jump to the end of the switch after the case (only if we're not the last case)
                if(i != node.getChildren().size()-1)
                    tacBlock.add(new TACEntry(endBlock.getBlockNum(), TACType.GOTO));
            }
        }

        //Add the end block
        tac.add(scope, endBlock);
        tacBlock = endBlock;
    }

    /**
     * Generates TAC for a While statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private void generateTACWhile(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        //We need two new blocks, one if the condition is true and another one if it is false (to jump to the end of the if)
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, trueBlock);
        tac.add(scope, falseBlock);

        //TODO: Add condition (first child)
        trueBlock.add(new TACEntry("a", "7", falseBlock.getBlockNum(), TACType.IFGEQ));

        //Traverse the true block & add the entries
        tacBlock = trueBlock;
        traverseTree(node.getChildren().get(1), tac, scope);

        //Add the jump to the condition
        tacBlock.add(new TACEntry(trueBlock.getBlockNum(), TACType.GOTO));

        //Add the false block to the end of the true block
        tacBlock = falseBlock;
    }

    /**
     * Generates TAC for a For statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private void generateTACFor(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        generateTACAssignacio(node.getChildren().get(0)); //First child is always assignation

        //We need two new blocks, one if the condition is true and another one if it is false (to jump to the end of the if)
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, trueBlock);
        tac.add(scope, falseBlock);

        //TODO: Add conditionn (second child)
        trueBlock.add(new TACEntry("a", "7", falseBlock.getBlockNum(), TACType.IFGEQ));

        //Traverse the true block & add the entries to the true block
        tacBlock = trueBlock;
        traverseTree(node.getChildren().get(3), tac, scope);

        //Add the increment to the end of the true block
        generateTACAssignacio(node.getChildren().get(2));

        //Add the jump to the beginning of the true block
        tacBlock.add(new TACEntry(trueBlock.getBlockNum(), TACType.GOTO));

        //Add the false block to the end of the true block
        tacBlock = falseBlock;
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

        //The last entry of the true block should jump to the end of the if
        int blockAfterIf = trueBlock.getBlockNum() + node.getChildren().size() - 1;
        //if (node.getChildren().get(node.getChildren().size() - 1).getToken().getType() == TokenType.ELSE) blockAfterIf++;
        tacBlock.add(new TACEntry(blockAfterIf, TACType.GOTO));

        tacBlock = falseBlock; //Update current block working on

        //Traverse all else if & else
        for(int i = 2; i < node.getChildren().size(); i++){
            generateTACElsif(node.getChildren().get(i), tac, scope, blockAfterIf);
        }


    }

    private void generateTACElsif(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope, int blockAfterIf){
        //We just need another new block (to jump if the condition is false)

        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, falseBlock);

        //TODO: Add condition (node.GetChildren().get(0))
        //If it is an ELSIF
        if(node.getToken().getType() == TokenType.ELSIF){
            tacBlock.add(new TACEntry("a", "7", falseBlock.getBlockNum(), TACType.IFGEQ));
            traverseTree(node.getChildren().get(1), tac, scope);

            //The last entry of this block should jump to the end of the if
            //Add the GOTO only if it is not the last ELSIF
            if(!node.getParent().getChildren().get(node.getParent().getChildren().size() - 1).equals(node))
                tacBlock.add(new TACEntry(blockAfterIf, TACType.GOTO));
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
