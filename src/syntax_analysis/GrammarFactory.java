package syntax_analysis;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.Filer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GrammarFactory {

    @Nullable
    public ArrayList<Production> getGrammar(@NotNull String filePath) throws IOException {

        ArrayList<Production> productions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();

        while(line != null){
            String[] splits = line.split(" ");
            ArrayList<ArrayList<Object>> derivations = new ArrayList<>();
            derivations.add(new ArrayList<>());
            int currentDerivation = 0;

            // Check if valid format
            if(splits.length <= 1 || !splits[1].equals("::=")){
                System.out.println("Error building the grammar");
                return null;
            }

            for(int i = 2; i < splits.length; i++){

                if(splits[i].equals("|")){
                    derivations.add(new ArrayList<>());
                    currentDerivation++;
                    continue;
                }


            }

            productions.add(new Production(splits[0], ));
            line = reader.readLine();
        }



        return productions;
    }
}
