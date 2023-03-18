package entities;

public class Token {

    private TokenType type;
    private final String data;
    //Mes coses

    public Token(TokenType type, String data){
        this.type = type;
        this.data = data;
    }
}
