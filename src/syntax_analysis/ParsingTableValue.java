package syntax_analysis;


import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public record ParsingTableValue(@NotNull Production production, int derivation) {

    public ParsingTableValue {
        Objects.requireNonNull(production);
    }

    @Override
    public String toString() {
        return "Production:" + this.production.getProducer() + " Derivation:" + this.derivation
                + " (" + production.getDerived().get(derivation) + ")";
    }
}
