package syntax_analysis;

import entities.TokenType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Production {

    private final String producer;
    private final List<ArrayList<Object>> derived;

    private ArrayList<TokenType> follows;
    private ArrayList<ArrayList<TokenType>> firsts;

    public Production(final String producer, ArrayList<ArrayList<Object>> derived){
        this.producer = producer;
        this.derived = derived;
    }


    public String getProducer() {
        return producer;
    }

    public List<ArrayList<Object>> getDerived() {
        return derived;
    }


    public void setFirsts(ArrayList<TokenType> firstsFromDerivation, int derivation){
            if(firsts == null) firsts = new ArrayList<>();
            firsts.add(derivation, firstsFromDerivation);
    }

    public void setFollows(ArrayList<TokenType> follows){
        this.follows = follows;
    }

    public @Nullable ArrayList<TokenType> getFirsts(int derivation){
        if(firsts == null) return null;
        return firsts.get(derivation);
    }

    public @Nullable ArrayList<TokenType> getFollows(){
        if(follows == null) return null;
        return follows;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production ").append(producer).append(" {\n");

        for(ArrayList<Object> al: derived){
            sb.append("\tor\n");
            for(Object o: al){
                sb.append("\t\t");
                sb.append(o instanceof String? "Non-terminal " + o: "Terminal " + ((TokenType)o).name());
                sb.append("\n");
            }
        }
        sb.append("}");

        return sb.toString();
    }
}
