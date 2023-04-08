package syntax_analysis;

import entities.TokenType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Production {

    private final String producer;
    private final List<ArrayList<Object>> derived;

    private List<TokenType> follows;
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
        //Delete repeated folllows
        this.follows = follows.stream().distinct().collect(Collectors.toList());
    }

    public @Nullable ArrayList<TokenType> getFirsts(int derivation){
        if(firsts == null) return null;
        return firsts.get(derivation);
    }

    public List<TokenType> getFollows(){
        if(follows == null) return null;
        return follows;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production ").append(producer).append(" {\n");

        for(int i = 0; i < derived.size(); i++){
            sb.append("\tor\n");
            for(Object o: derived.get(i)){
                sb.append("\t\t");
                sb.append(o instanceof String? "Non-terminal " + o: "Terminal " + ((TokenType)o).name());
                sb.append("\n");
            }
            if(firsts != null){
                sb.append("\t\t\tWith Firsts: ");
                for(int j = 0; j < firsts.get(i).size(); j++){
                    if(firsts.get(i) != null)
                        sb.append(firsts.get(i).get(j)).append(" ");
                }
                sb.append("\n");
            }
        }
        if(follows != null){
            sb.append("\tWith Follows: ");
            for(TokenType t: follows)
                sb.append(t.name()).append(" ");
            sb.append("\n");
        }

        sb.append("}");

        return sb.toString();
    }

    public boolean produces(String next) {
        for(ArrayList<Object> al: derived){
            for(Object o: al){
                if(o instanceof String && next.equals(o))
                    return true;
            }
        }

        return false;
    }

    public @Nullable ArrayList<TokenType> getAllFirsts() {
        ArrayList<TokenType> allFirsts = new ArrayList<>();
        for(ArrayList<TokenType> al: firsts)
            allFirsts.addAll(al);

        return allFirsts;
    }
}
