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
        TACBlock tacBlock = new TACBlock();
        tac.add("main", tacBlock);
        traverseTree(pt.getRoot(), tacBlock);

        if(tacOptimizer != null)
            tac = tacOptimizer.optimizeTAC(tac);

        if(outputFile)
            writeToFile(tac.toString());

        return tac;
    }

    private void traverseTree(@NotNull ParseTreeNode node, @NotNull TACBlock tacBlock){

        if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node, tacBlock);
            return;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return;
        for(ParseTreeNode child: node.getChildren())
            traverseTree(child, tacBlock);
    }

    private void generateTACAssignacio(@NotNull ParseTreeNode node, @NotNull TACBlock tacBlock){
        TACEntry entry;
        if(node.getChildren().size() == 2){ //Special case: ++ or --
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(0).getToken().getData(),
                    "1",
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2), tacBlock);

            //All the temporary values calculated by the traversal are stored in WORK_REG
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    WORK_REG,
                    TACType.EQU);
        }
        else{
            entry = new TACEntry(node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(0).getToken().getData(),
                    node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }

        //Add the entry to the block
        tacBlock.add(entry);
    }

    private void traverseTACAssignacio(@NotNull ParseTreeNode node, @NotNull TACBlock tacBlock){
        TACEntry entry;

        //If the first child is equ, go down the tree
        if(node.getChildren().get(0).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(0), tacBlock);

            //t = (t-1) OP ch2
            entry = new TACEntry(WORK_REG, WORK_REG, node.getChildren().get(2).getToken().getData(),
                    TACType.getType(node.getChildren().get(1).getToken().getType()));
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2), tacBlock);

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
