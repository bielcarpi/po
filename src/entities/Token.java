package entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Token class represents a single Token, together with its {@link TokenType} and related data.
 *
 * @see TokenType
 * @see TokenStream
 */
public class Token {

    private TokenType type;
    private final String data;
    private final int line, column;

    /**
     * Token Constructor
     * @param type The {@link TokenType} that the Token represents
     * @param data The data that this Token contains. Can be {@code null}.
     */
    public Token(@NotNull TokenType type, @Nullable String data, int line, int column){
        this.type = type;
        this.data = data;
        this.line = line;
        this.column = column;
    }


    @Override
    public String toString() {
        return "Token {Type: %s, Data: %s}".formatted(type, data);
    }


    public TokenType getType() {
        return type;
    }
    public String getData() {
        return data;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
