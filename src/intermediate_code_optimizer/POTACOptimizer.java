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

                    TACEntry newEntry = null;

                    // We assume that a = 0 - a -> a = a
                    if((tacEntry.getType() == TACType.ADD) || (tacEntry.getType() == TACType.SUB)){
                        // t1 = 0 +/- t2 -> t1 = t2
                        if(tacEntry.getArg1() != null && tacEntry.getArg1().equals("0") && !tacEntry.getArg2().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),nextBlock.get(i).getArg2(), TACType.EQU);
                        }
                        // t1 = 0 +/- t2 -> t1 = t2
                        if(tacEntry.getArg2() != null && tacEntry.getArg2().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),nextBlock.get(i).getArg1(), TACType.EQU);
                        }
                    }else if((tacEntry.getType() == TACType.MUL)){
                        // t1 = 0 * t2 -> t1 = 0 || t1 = 0 * t2 -> t1 = 0
                        if((tacEntry.getArg1() != null && tacEntry.getArg1().equals("0")) || (tacEntry.getArg2() != null && tacEntry.getArg2().equals("0"))){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),"0", TACType.EQU);
                        }
                    }else if((tacEntry.getType() == TACType.DIV)){
                        // t1 = t2 / 0 -> "infinity" In MIPS, this is 4294967296 (2^32)
                        if(tacEntry.getArg1() != null && tacEntry.getArg1().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),"0", TACType.EQU);
                        }
                        // t1 = 0 / t2 -> t1 = 0
                        if(tacEntry.getArg2() != null && tacEntry.getArg2().equals("0")){
                            newEntry = new TACEntry(tacEntry.getScope(), nextBlock.get(i+1).getDest(),"4294967296", TACType.EQU);
                        }
                    }

                    // Replace to an optimized entry
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
}
