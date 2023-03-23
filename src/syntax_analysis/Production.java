package syntax_analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Production {

    private final String producer;
    private final List<ArrayList<Object>> derived;

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
}
