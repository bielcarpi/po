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
    private TokenType lastToken;

    /**
     * Constructor to instance a new Token Stream.
     */
    public TokenStream(){
        tokens = new java.util.LinkedList<>();
        lastToken = null;
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
        if((lastToken == null || lastToken == TokenType.EOL) && token.getType() == TokenType.EOL) return;

        lastToken = token.getType();
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

    /**
     * Returns a string representation of the stream.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tokens.size(); i++){
            sb.append(tokens.toArray()[i]);
            if(i != tokens.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
