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

    private final Queue<Token> tokens;

    /**
     * Constructor to instance a new Token Stream.
     */
    public TokenStream(){
        tokens = new java.util.LinkedList<>();
    }

    /**
     * Gets the next token in the stream without removing it.
     * @return The next token in the stream.
     */
    public Token peekToken(){
        return tokens.peek();
    }

    /**
     * Gets the next token in the stream and removes it.
     * @return The next token in the stream.
     */
    public Token nextToken(){
        return tokens.poll();
    }

    /**
     * Adds a new token to the stream.
     * @param token The token to be added.
     */
    public void addNewToken(Token token){
        tokens.add(token);
    }

    /**
     * Checks if the stream is empty.
     * @return {@code true} if the stream is empty, {@code false} otherwise.
     */
    public boolean isEmpty(){
        return tokens.isEmpty();
    }

    /**
     * Prints the stream to the console.
     */
    public void printStream(){
        for(Token t: tokens){
            System.out.println(t);
        }
    }
}
