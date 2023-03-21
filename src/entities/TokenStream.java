package entities;

import java.util.Queue;

/**
 * The TokenStream class represents a stream of {@link Token}.
 * Acting as a {@link Queue}, it wraps useful functions for manipulation of a stream of tokens.
 *
 * @see Token
 * @see TokenType
 */
public class TokenStream {

    private Queue<Token> tokens;

    public TokenStream(){
    }


    public Token nextToken(){
        return null;
    }

    public void addNewToken(Token token){
        System.out.println("New token added -> " + token);
    }

    public boolean isEmpty(){
        return true;
    }
}
