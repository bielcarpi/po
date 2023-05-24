package intermediate_code_optimizer;

import entities.TAC;
import entities.TACBlock;
import entities.TACEntry;
import entities.TACType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class POTACOptimizer implements TACOptimizer{

    @Override
    public @NotNull TAC optimizeTAC(@NotNull TAC tac) {
        for (String funcName : tac.getEntries().keySet()) {
            for (TACBlock block : tac.getEntries().get(funcName)) {
                ArrayList<TACEntry> nextBlock = block.getEntries();
                for(int i=0; i<nextBlock.size(); i++){
                    TACEntry tacEntry = nextBlock.get(i);

                    // Remove zero operations
                    if(removeZeroOperations(tacEntry)){
                        nextBlock.remove(i);        // Remove the zero operation
                        i--;
                        nextBlock.remove(i+1); // Remove the next assignation entry
                    }
                }

            }
        }
        return tac;
    }

    private boolean removeZeroOperations(TACEntry tacEntry) {

        if((tacEntry.getType() == TACType.ADD
            || tacEntry.getType() == TACType.MUL
            || tacEntry.getType() == TACType.SUB
            || tacEntry.getType() == TACType.DIV)){
            /*
                Now it removes the zero operations from the TAC
                Example:
                t1 = 0 + t2
                t1 = t2 + 0
                t1 = 0 - t2
                t1 = t2 - 0
                t1 = 0 * t2
                t1 = t2 * 0
                t1 = 0 / t2
                t1 = t2 / 0
                t1 = 0 * t2
                t1 = t2 * 0
                t1 = 0 / t2
            */

            if(tacEntry.getArg1() != null && tacEntry.getArg1().equals("0") ||
               tacEntry.getArg2() != null && tacEntry.getArg2().equals("0")){
                System.out.println("Removing zero operation: " + tacEntry);
                return true;
            }
        }
        return false;
    }
}
