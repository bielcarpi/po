package entities;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * The TokenType enum lists all types of Tokens we can have, and defines regex for each
 * one of them.
 * It also provides {@link #getMatch(String)}, which will return a TokenType given a string.
 *
 * @see Token
 */
public enum TokenType {
    IF(Pattern.compile("^if$")),
    ELSE(Pattern.compile("^else$")),
    ELSIF(Pattern.compile("^elsif$")),
    SWITCH(Pattern.compile("^switch$")),
    OPT(Pattern.compile("^opt$")),
    ARROW(Pattern.compile("^->$")),
    DEFAULT(Pattern.compile("^default$")),
    TYPE(Pattern.compile("^type$")),
    WHILE(Pattern.compile("^while$")),
    FOR(Pattern.compile("^for$")),
    IN(Pattern.compile("^in$")),
    CONTINUE(Pattern.compile("^continue$")),
    BREAK(Pattern.compile("^break$")),
    FUNC(Pattern.compile("^func$")),
    RET(Pattern.compile("^ret$")),
    MAIN(Pattern.compile("^main$")),
    VAR(Pattern.compile("^var$")),
    ADD(Pattern.compile("^\\+$")),
    SUB(Pattern.compile("^-$")),
    DIV(Pattern.compile("^/$")),
    MULT(Pattern.compile("^\\*$")),
    MOD(Pattern.compile("^%$")),
    DOUBLE_EQU(Pattern.compile("^==$")),
    DOUBLE_ADD(Pattern.compile("^\\+\\+$")),
    DOUBLE_SUB(Pattern.compile("^--$")),
    DIFF(Pattern.compile("^!=$")),
    EQU(Pattern.compile("^=$")),
    LT(Pattern.compile("^<$")),
    GT(Pattern.compile("^>$")),
    LTE(Pattern.compile("^<=$")),
    GTE(Pattern.compile("^>=$")),
    NOT(Pattern.compile("^not$")),
    AND(Pattern.compile("^and$")),
    OR(Pattern.compile("^or$")),
    POINT(Pattern.compile("^\\.$")),
    COMMA(Pattern.compile("^,$")),
    SEMICOLON(Pattern.compile("^;$")),
    OPEN_PAREN(Pattern.compile("^\\($")),
    CLOSE_PAREN(Pattern.compile("^\\)$")),
    OPEN_BRACKET(Pattern.compile("^\\[$")),
    CLOSE_BRACKET(Pattern.compile("^\\]$")),
    OPEN_BRACE(Pattern.compile("^\\{$")),
    CLOSE_BRACE(Pattern.compile("^\\}$")),
    INT(Pattern.compile("^\\d+$")),
    FLOAT(Pattern.compile("^\\d+\\.\\d+$")),
    STRING(Pattern.compile("^\".*\"$")),
    TRUE(Pattern.compile("^true$")),
    FALSE(Pattern.compile("^false$")),
    EOL(Pattern.compile("^\\n$")),
    ID(Pattern.compile("^[a-zA-Z_]\\w*$")),
    EOF(null),
    VOID(null),
    UNKNOWN(null);

    private final Pattern regex;

    /**
     * TokenType Constructor
     * @param regex The regex that identifies this TokenType
     */
    TokenType(Pattern regex){
        this.regex = regex;
    }

    /**
     * Given a String, identifies whether this TokenType matches it
     * @param token The string to check for matching
     * @return Whether this TokenType matches the string provided
     */
    private boolean matches(String token){
        if(regex == null) return false;
        return regex.matcher(token).matches();
    }

    /**
     * Given a String, returns the TokenType that matches it
     * @param token The string to check for matching
     * @return The TokenType that matches the String provided. Can be {@code null}.
     */
    @Nullable
    public static TokenType getMatch(String token){
        for(TokenType type : TokenType.values())
            if(type.matches(token)) return type;

        return null;
    }
}
