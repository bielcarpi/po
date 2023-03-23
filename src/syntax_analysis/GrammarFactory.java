package syntax_analysis;


import entities.Token;
import entities.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.Filer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GrammarFactory {

    @Nullable
    public ArrayList<Production> getGrammar(@NotNull String filePath) {

        try {
            ArrayList<Production> productions = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while (line != null) {
                String[] splits = line.split(" ");
                ArrayList<ArrayList<Object>> derivations = new ArrayList<>();
                derivations.add(new ArrayList<>());
                int currentDerivation = 0;

                // Check if valid format
                if (splits.length <= 1 || !splits[1].equals("::="))
                    return null;

                for (int i = 2; i < splits.length; i++) {

                    if (splits[i].equals("|")) {
                        derivations.add(new ArrayList<>());
                        currentDerivation++;
                        continue;
                    }
                    // Check for empty spaces
                    else if (splits[i].trim().isEmpty()) {
                        continue;
                    }

                    TokenType type = TokenType.getMatch(splits[i]);
                    if(splits[i].equals("ε")) type = TokenType.VOID;
                    else if(splits[i].equals("\\n")) type = TokenType.EOL;
                    derivations.get(currentDerivation).add(type != null? type: splits[i]);
                }

                productions.add(new Production(splits[0], derivations));
                line = reader.readLine();
            }

            return productions;
        }
        catch(IOException e){
            return null;
        }
    }
}
