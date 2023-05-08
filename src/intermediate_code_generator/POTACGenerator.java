package intermediate_code_generator;

import entities.*;
import intermediate_code_optimizer.TACOptimizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;

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
    public @NotNull String generateTAC(@NotNull ParseTree pt) {
        StringBuilder tac = new StringBuilder();
        traverseTree(pt.getRoot(), tac);

        //if(tacOptimizer != null)
            //tac = tacOptimizer.optimizeTAC(tac.toString());

        if(outputFile)
            writeToFile(tac.toString());

        return tac.toString();
    }

    private void traverseTree(@NotNull ParseTreeNode node, @NotNull StringBuilder tac){

        if(node.getSelf().equals("assignacio")){
            generateTACAssignacio(node, tac);
            return;
        }

        if(node.getChildren() == null || node.getChildren().isEmpty()) return;
        for(ParseTreeNode child: node.getChildren())
            traverseTree(child, tac);
    }

    private void generateTACAssignacio(@NotNull ParseTreeNode node, @NotNull StringBuilder tac){
        if(node.getSelf().equals("assignacio")){
            if(node.getChildren().get(2).getSelf().equals("exp")){
                tac.append(node.getChildren().get(0).getSelf().toString())
                        .append(" = ")
                        .append("t")
                        .append(tTimes++)
                        .append("\n");

                generateTACAssignacio(node.getChildren().get(2), tac);
            }
            else{
                tac.append(node.getChildren().get(0).getSelf().toString())
                        .append(" = ")
                        .append(node.getChildren().get(2).getSelf().toString())
                        .append("\n");
            }
        }
        else{ //If exp
            if(!node.getChildren().get(0).getSelf().equals("exp")){
                tac.append("t")
                        .append(tTimes++)
                        .append(" = ")
                        .append(node.getChildren().get(0).getSelf().toString())
                        .append(" ")
                        .append(node.getChildren().get(1).getSelf().toString())
                        .append(" ")
                        .append(node.getChildren().get(2).getSelf().toString())
                        .append("\n");
                return;
            }

            tac.append("t")
                    .append(tTimes-1)
                    .append(" = ")
                    .append("t")
                    .append(tTimes++)
                    .append(" ")
                    .append(node.getChildren().get(1).getSelf().toString())
                    .append(" ")
                    .append(node.getChildren().get(2).getSelf().toString())
                    .append("\n");

            generateTACAssignacio(node.getChildren().get(0), tac);
        }
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
