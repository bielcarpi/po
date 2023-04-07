package syntax_analysis;


import entities.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ParsingTableTuple(@NotNull String producer, @NotNull TokenType terminal) {

    public ParsingTableTuple {
        Objects.requireNonNull(producer);
        Objects.requireNonNull(terminal);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParsingTableTuple other))
            return false;

        return this.producer.equals(other.producer) && this.terminal.name().equals(other.terminal.name());
    }

    @Override
    public String toString() {
        return "row:" + this.producer + " col:" + this.terminal.name();
    }
}
