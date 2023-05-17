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
    private int tTimes = 0; //The number of times a temporary variable has been used for TAC generation

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
        traverseTree(pt.getRoot(), tac);

        if(tacOptimizer != null)
            tac = tacOptimizer.optimizeTAC(tac);

        if(outputFile)
            writeToFile(tac.toString());

        return tac;
    }

    private void traverseTree(@NotNull ParseTreeNode node, @NotNull TAC tac){

        if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node, tac);
            return;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return;
        for(ParseTreeNode child: node.getChildren())
            traverseTree(child, tac);
    }

    private void generateTACAssignacio(@NotNull ParseTreeNode node, @NotNull TAC tac){
        if(node.getChildren().size() == 2){ //Special case: ++ or --
            tac.append("t")
                    .append(tTimes)
                    .append(" = ")
                    .append(node.getChildren().get(0).getToken().getData())
                    .append(" ")
                    .append(node.getChildren().get(1).getToken().getType().toPrettyString().charAt(0))
                    .append(" 1\n");

            tac.append(node.getChildren().get(0).getToken().getData())
                    .append(" = ")
                    .append("t")
                    .append(tTimes)
                    .append("\n");

            tTimes++;
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2), tac);

            tac.append(node.getChildren().get(0).getToken().getData())
                    .append(" = ")
                    .append("t")
                    .append(tTimes-1)
                    .append("\n");
        }
        else{
            tac.append(node.getChildren().get(0).getToken().toString())
                    .append(" = ")
                    .append(node.getChildren().get(0).getToken().getData())
                    .append(" ")
                    .append(node.getChildren().get(1).getToken().getType().toPrettyString())
                    .append(" ")
                    .append(node.getChildren().get(2).getToken().getData())
                    .append("\n");
        }
    }

    private void traverseTACAssignacio(@NotNull ParseTreeNode node, @NotNull TAC tac){
        //If the first child is equ, go down the tree
        if(node.getChildren().get(0).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(0), tac);

            tac.append("t")
                    .append(tTimes)
                    .append(" = ")
                    .append("t")
                    .append(tTimes-1)
                    .append(" ")
                    .append(node.getChildren().get(1).getToken().getType().toPrettyString())
                    .append(" ")
                    .append(node.getChildren().get(2).getToken().getData())
                    .append("\n");
        }
        else if(node.getChildren().get(2).getSelf().equals("exp")){
            traverseTACAssignacio(node.getChildren().get(2), tac);

            tac.append("t")
                    .append(tTimes)
                    .append(" = ")
                    .append(node.getChildren().get(0).getToken().getData())
                    .append(" ")
                    .append(node.getChildren().get(1).getToken().getType().toPrettyString())
                    .append(" t")
                    .append(tTimes-1)
                    .append("\n");
        }
        else{
            tac.append("t")
                    .append(tTimes)
                    .append(" = ")
                    .append(node.getChildren().get(0).getToken().getData())
                    .append(" ")
                    .append(node.getChildren().get(1).getToken().getType().toPrettyString())
                    .append(" ")
                    .append(node.getChildren().get(2).getToken().getData())
                    .append("\n");
        }

        tTimes++;
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
