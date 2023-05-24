package intermediate_code_generator;

import entities.*;
import intermediate_code_optimizer.TACOptimizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
    private static final String WORK_REG = "s0";
    private TACBlock tacBlock; //Current block aux for the traverseTree method

    private final ArrayList<Syscall> syscallsList; //Syscalls used in the program. They will be added as separate functions

    /**
     * Default constructor
     * @param tacOptimizer the optimizer to be used
     * @param outputFile if true, the TAC generated in {@link #generateTAC(ParseTree)} will be written to a file called "output.tac"
     */
    public POTACGenerator(@Nullable TACOptimizer tacOptimizer, boolean outputFile){
        this.tacOptimizer = tacOptimizer;
        this.outputFile = outputFile;
        syscallsList = new ArrayList<>();
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
            tac.add(SymbolTable.GLOBAL_SCOPE, tacBlock);

            //The program can have either assignations or functions as children
            //We'll first loop through the assignations and then through the functions
            for (ParseTreeNode child : pt.getRoot().getChildren()) {
                if (child.getSelf().equals("assignacio")) {
                    generateTACAssignacio(child, SymbolTable.GLOBAL_SCOPE);
                }
            }

            for (ParseTreeNode child : pt.getRoot().getChildren()){
                if (!child.getSelf().equals("assignacio")) {
                    tacBlock = new TACBlock(false);
                    tac.add(child.getToken().getData(), tacBlock);
                    traverseTree(child, tac, child.getToken().getData()); //Traverse the function and generate TAC inside the block

                    //Add a return at the end of the function if it doesn't have one
                    if(tacBlock.getEntries().isEmpty() || tacBlock.getEntries().get(tacBlock.getEntries().size() - 1).getType() != TACType.RET)
                        tacBlock.add(new TACEntry(child.getToken().getData(), null, "0", TACType.RET));
                }
            }
        }

        //Add syscall functions to TAC (only if they are used)
        if(!syscallsList.isEmpty()){
            for (Syscall syscall: syscallsList) {
                TACBlock syscallBlock = new TACBlock(false);
                syscallBlock.add(new TACEntry(Syscall.getID(syscall), TACType.SYSCALL));
                syscallBlock.add(new TACEntry(SymbolTable.GLOBAL_SCOPE, null, "0", TACType.RET));
                tac.add(syscall.name(), syscallBlock);
            }
        }

        if(tacOptimizer != null)
            tac = tacOptimizer.optimizeTAC(tac);

        if(outputFile)
            writeToFile(tac.toString());

        return tac;
    }

    private int traverseTree(@NotNull ParseTreeNode node, @NotNull TAC tac, String scope){

        if(node.getToken() != null){
            switch (node.getToken().getType()) {
                case IF -> {
                    return generateTACIf(node, tac, scope);
                }
                case SWITCH -> {
                    generateTACSwitch(node, tac, scope);
                    return -1;
                }
                case FOR -> {
                    generateTACFor(node, tac, scope);
                    return -1;
                }
                case WHILE -> {
                    generateTACWhile(node, tac, scope);
                    return -1;
                }
                case ID -> { //Function CALL
                    //Only if it is a function call, not a function declaration
                    if(!node.getParent().getSelf().equals("<programa>")){
                        generateTACFuncCall(node, scope);
                        return -1;
                    }
                }
                case RET -> {
                    tacBlock.add(new TACEntry(scope, null, node.getChildren().get(0).getToken().getData(), TACType.RET));
                    return -1;
                }
                case BREAK -> { //Converted to GOTO
                    tacBlock.add(new TACEntry(tacBlock.getBlockNum()+1, TACType.GOTO));
                    return -1;
                }
                case CONTINUE -> { //Converted to GOTO
                    tacBlock.add(new TACEntry(tacBlock.getBlockNum(), TACType.GOTO));
                    return -1;
                }
            }
        }

        if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node, scope);
            return -1;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return -1;

        int highestBlock = -1;
        for(ParseTreeNode child: node.getChildren()){
            int block = traverseTree(child, tac, scope);
            if(block > highestBlock) highestBlock = block;
        }

        return highestBlock;
    }

    /**
     * Generates TAC for a Function CALL
     * @param node the node of the function
     */
    private void generateTACFuncCall(@NotNull ParseTreeNode node, @NotNull String scope){
        //If it's a syscall, and we haven't detected it yet, add it to the syscalls list
        if(Syscall.isSyscall(node.getToken().getData()) && !syscallsList.contains(Syscall.get(node.getToken().getData())))
            syscallsList.add(Syscall.get(node.getToken().getData()));

        tacBlock.add(new TACEntry(-1, TACType.SAVE_CONTEXT));
        //If we have params, add all childs as parameters
        if(node.getChildren() != null){
            for(int i = 0; i < node.getChildren().size(); i++) {
                // TODO: here only supported params of type ID
                tacBlock.add(new TACEntry(scope, null,
                        node.getChildren().get(i).getToken().getData(), i, TACType.ADD_PARAM));
            }
        }
        tacBlock.add(new TACEntry(null, null, node.getToken().getData(), TACType.CALL));
        tacBlock.add(new TACEntry(-1, TACType.LOAD_CONTEXT));
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
            tacBlock.add(new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
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

        trueBlock.add(new TACEntry(scope, node.getChildren().get(0).getChildren().get(0).getToken().getData(),
                node.getChildren().get(0).getChildren().get(2).getToken().getData(),
                falseBlock.getBlockNum(),
                TACType.GetAntonym(node.getChildren().get(0).getChildren().get(1).getToken().getType())));

        //Traverse the true block & add the entries
        tacBlock = trueBlock;
        traverseTree(node.getChildren().get(1), tac, scope);

        //Add the jump to the condition
        tacBlock.add(new TACEntry(trueBlock.getBlockNum(), TACType.GOTO));

        //Add the false block to the end of the true block
        tac.add(scope, falseBlock);
        tacBlock = falseBlock;
    }

    /**
     * Generates TAC for a For statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private void generateTACFor(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        generateTACAssignacio(node.getChildren().get(0), scope); //First child is always assignation

        //We need two new blocks, one if the condition is true and another one if it is false (to jump to the end of the if)
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, trueBlock);

        trueBlock.add(new TACEntry(scope, node.getChildren().get(1).getChildren().get(0).getToken().getData(),
                node.getChildren().get(1).getChildren().get(2).getToken().getData(),
                falseBlock.getBlockNum(),
                TACType.GetAntonym(node.getChildren().get(1).getChildren().get(1).getToken().getType())));

        //Traverse the true block & add the entries to the true block
        tacBlock = trueBlock;
        traverseTree(node.getChildren().get(3), tac, scope);

        //Add the increment to the end of the true block
        generateTACAssignacio(node.getChildren().get(2), scope);

        //Add the jump to the beginning of the true block
        tacBlock.add(new TACEntry(trueBlock.getBlockNum(), TACType.GOTO));

        //Add the false block to the end of the true block
        tac.add(scope, falseBlock);
        tacBlock = falseBlock;
    }


    /**
     * Generates TAC for an if statement
     * @param node the node to be traversed
     * @param tac the TAC data structure
     * @param scope to obtain the scope for the new blocks
     */
    private int generateTACIf(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope){
        //We need two new blocks, one if the condition is true and another one if it is false (to jump to the end of the if)
        TACBlock conditionBlock = new TACBlock(true);
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, conditionBlock);

        //Generate the condition
        generateTACCondition(conditionBlock, trueBlock, falseBlock, node.getChildren().get(0), scope);

        tac.add(scope, trueBlock);

        //Traverse the true block & add the entries to the true block
        tacBlock = trueBlock; //Update current block working on
        int blockAfterIf = traverseTree(node.getChildren().get(1), tac, scope);

        //If there is no block after the if, compute this one
        if(blockAfterIf == -1) blockAfterIf = trueBlock.getBlockNum() + node.getChildren().size() - 1;
        else blockAfterIf += node.getChildren().size() - 2;

        //The last entry of the true block should jump to the end of the if
        if(node.getChildren().size() > 2)
            tacBlock.add(new TACEntry(blockAfterIf, TACType.GOTO));

        tac.add(scope, falseBlock);
        tacBlock = falseBlock; //Update current block working on

        //Traverse all else if & else
        for(int i = 2; i < node.getChildren().size(); i++){
            generateTACElsif(node.getChildren().get(i), tac, scope, blockAfterIf);
        }

        return blockAfterIf;
    }

    private void generateTACElsif(@NotNull ParseTreeNode node, @NotNull TAC tac, @NotNull String scope, int blockAfterIf){
        //We just need another new block (to jump if the condition is false)
        TACBlock trueBlock = new TACBlock(true);
        TACBlock falseBlock = new TACBlock(true);
        tac.add(scope, trueBlock);
        tac.add(scope, falseBlock);

        //If it is an ELSIF
        if(node.getToken().getType() == TokenType.ELSIF){
            generateTACCondition(tacBlock, trueBlock, falseBlock, node.getChildren().get(0), scope);
            tacBlock = trueBlock; //Update current block working on
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
    private void generateTACAssignacio(@NotNull ParseTreeNode node, @NotNull String scope){
        TACEntry entry;
        if(node.getChildren().size() == 2){ //Special case: ++ or --
            entry = new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(0).getToken().getData(),
                    "1",
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){ //x = z OP exp
            traverseTACAssignacio(node.getChildren().get(2), scope);

            //All the temporary values calculated by the traversal are stored in WORK_REG
            entry = new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
                    WORK_REG,
                    TACType.EQU);
        }
        else{ //x = z
            //If z is a function call, we need to call it
            if(SymbolTable.getInstance().lookup(node.getChildren().get(2).getToken().getData(), SymbolTable.GLOBAL_SCOPE) instanceof SymbolTableFunctionEntry){
                generateTACFuncCall(node.getChildren().get(2), scope);
                entry = new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
                        "v0",
                        TACType.EQU);
            }
            else{ //If z is a variable, we just need to copy it
                entry = new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
                        node.getChildren().get(2).getToken().getData(),
                        TACType.getType(node.getChildren().get(1).getToken().getType()));
            }
        }

        //Add the entry to the block
        tacBlock.add(entry);
    }

    /**
     * This function generates TAC code for a conditional with up to 2 conditions
     * @param conditionBlock Block where the condition is evaluated
     * @param trueBlock Block where the code is executed if the condition is true
     * @param falseBlock Block where  the code is executed if the condition is false
     * @param node Node to be traversed
     * @param scope Scope of the block
     */
    private void generateTACCondition(@NotNull TACBlock conditionBlock, @NotNull TACBlock trueBlock,
                                      @NotNull TACBlock falseBlock, @NotNull ParseTreeNode node,
                                      String scope) {
        TokenType type = node.getChildren().get(1).getToken().getType();
        if (type != TokenType.OR && type != TokenType.AND) {
            conditionBlock.add(new TACEntry(scope, node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(2).getToken().getData(),
                    falseBlock.getBlockNum(),
                    TACType.GetAntonym(node.getChildren().get(1).getToken().getType())));
            return;
        }
        switch (type) {
            case OR -> {
                // OR block will always have an exp on the 1s and 3d node. One of this two nodes is a concrete
                conditionBlock.add(new TACEntry(scope, node.getChildren().get(0).getChildren().get(0).getToken().getData(),
                        node.getChildren().get(0).getChildren().get(2).getToken().getData(),
                        trueBlock.getBlockNum(),
                        TACType.GetEquivalent(node.getChildren().get(0).getChildren().get(1).getToken().getType())));
                generateTACCondition(conditionBlock, trueBlock, falseBlock, node.getChildren().get(2), scope);
            }
            case AND -> {
                conditionBlock.add(new TACEntry(scope, node.getChildren().get(0).getChildren().get(0).getToken().getData(),
                        node.getChildren().get(0).getChildren().get(2).getToken().getData(),
                        falseBlock.getBlockNum(),
                        TACType.GetAntonym(node.getChildren().get(0).getChildren().get(1).getToken().getType())));
                generateTACCondition(conditionBlock, trueBlock, falseBlock, node.getChildren().get(2), scope);
            }
        }
    }


    /**
     * Traverses the tree and generates TAC for the assignations
     * @param node the node to be traversed
     */
    private void traverseTACAssignacio(@NotNull ParseTreeNode node, String scope){
        TACEntry entry;

        //If the first child is equ, go down the tree
        if(node.getChildren().get(0).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(0), scope);

            //t = (t-1) OP ch2
            entry = new TACEntry(scope, WORK_REG, WORK_REG, node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2), scope);

            //t = ch0 OP (t-1)
            entry = new TACEntry(scope, WORK_REG, node.getChildren().get(0).getToken().getData(), WORK_REG,
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else{
            //t = ch0 OP ch2
            entry = new TACEntry(scope, WORK_REG, node.getChildren().get(0).getToken().getData(),
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
