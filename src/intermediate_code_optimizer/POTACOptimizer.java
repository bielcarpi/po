package intermediate_code_optimizer;

import entities.TAC;
import entities.TACBlock;
import entities.TACEntry;
import entities.TACType;
import mips_generator.MIPSConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class POTACOptimizer implements TACOptimizer{

    @Override
    public @NotNull TAC optimizeTAC(@NotNull TAC tac) {
        for (String funcName : tac.getEntries().keySet()) {
            for (TACBlock block : tac.getEntries().get(funcName)) {
                ArrayList<TACEntry> nextBlock = block.getEntries();

                for(int i=0; i<nextBlock.size(); i++){
                    TACEntry tacEntry = nextBlock.get(i);

                    TACEntry newEntry = null;

                    if(tacEntry.getType() == TACType.ADD){
                        // t1 = 0 + t2 -> t1 = t2
                        if(tacEntry.getArg1() != null && tacEntry.getArg1().equals("0") && !tacEntry.getArg2().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),nextBlock.get(i).getArg2(), TACType.EQU);
                        }
                        // t1 = 0 + t2 -> t1 = t2
                        if(tacEntry.getArg2() != null && tacEntry.getArg2().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),nextBlock.get(i).getArg1(), TACType.EQU);
                        }
                    }

                    if(newEntry != null){
                        nextBlock.remove(i);
                        nextBlock.remove(i);
                        nextBlock.add(i, newEntry);
                        i--;
                    }
                }
            }
        }
        return tac;
    }

    private TACEntry removeZeroOperations(TACEntry tacEntry) {
        /*if((tacEntry.getType() == TACType.ADD)){

            // t1 = 0 + t2 -> t1 = t2
            if(tacEntry.getArg1() != null && tacEntry.getArg1().equals("0") && !tacEntry.getArg2().equals("0")){
                System.out.println("Removing zero operation: " + tacEntry);
                return new TACEntry(tacEntry.getScope(), tacEntry.getDest(), tacEntry.getArg2(), TACType.EQU);
            }

            // t1 = t2 + 0 -> t1 = t2
            if(tacEntry.getArg2() != null && tacEntry.getArg2().equals("0")){
                System.out.println("Removing zero operation: " + tacEntry);
                return new TACEntry(null, null, null, null);
            }
        }*/
        return new TACEntry(null, null, null, null);
    }
}
