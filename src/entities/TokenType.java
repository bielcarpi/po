package entities;

import java.util.regex.Pattern;

public enum TokenType {
    //TODO: Add all tokens, and regex correctly
    IF(Pattern.compile("^if$")),
    WHILE(Pattern.compile("^while$"));

    private final Pattern regex;

    TokenType(Pattern regex){
        this.regex = regex;
    }

    boolean matches(String token){
        return false;
    }

    public static TokenType getMatch(String token){
        return null;
    }

}
