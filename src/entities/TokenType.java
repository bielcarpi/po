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
    //TODO: Add all tokens, and regex correctly
    IF(Pattern.compile("^if$")),
    WHILE(Pattern.compile("^while$"));

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
        return false;
    }

    /**
     * Given a String, returns the TokenType that matches it
     * @param token The string to check for matching
     * @return The TokenType that matches the String provided. Can be {@code null}.
     */
    @Nullable
    public static TokenType getMatch(String token){
        return null;
    }

}
