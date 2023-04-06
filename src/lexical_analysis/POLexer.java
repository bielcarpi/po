package lexical_analysis;

import entities.Token;
import entities.TokenStream;
import entities.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class POLexer implements Lexer {

    private final static Pattern specialCharacters = Pattern.compile("([ \\t,!(){}=\\[\\]+\\-*/\\n<>;])");
    private final static Pattern doubleOperators = Pattern.compile("([=+\\-])");
    private final static String error = "Error on line %d column %d. %s is not a valid token.\n";

    @Override
    @Nullable
    public TokenStream generateTokenStream(@NotNull String program) {
        TokenStream ts = new TokenStream();
        StringBuilder sb = new StringBuilder();
        int row = 1, col = 1;
        boolean insideQuotes = false; //Flag if we're inside a "" (quotes) bloc

        char[] chars = program.toCharArray();

        //Analyze char by char the program
        for(int i = 0; i < chars.length; i++, col++){
            if(chars[i] == '\n') row++;

            //If we're inside quotes, skip everything until we find another quote
            if(chars[i] == '"'){
                insideQuotes = !insideQuotes;
                sb.append(chars[i]);
                continue;
            }
            else if(insideQuotes){
                sb.append(chars[i]);
                continue;
            }
            //If a special character is found
            if(specialCharacters.matcher(Character.toString(chars[i])).matches()){
                //Check the token in the buffer
                if(sb.length() > 0){
                    TokenType tokenType = TokenType.getMatch(sb.toString());
                    if(tokenType == null) //Print an error. We won't add anything to the TokenStream
                        errorFound(row, col - sb.length(), sb.toString());

                    else //Add the new token to the TokenStream
                        ts.addNewToken(new Token(tokenType, sb.toString()));

                }


                //Check whether this special character found is a token itself
                TokenType tokenType = TokenType.getMatch(Character.toString(chars[i]));
                sb.append(chars[i]);
                if(tokenType != null) {
                    // If the next character is a double operator, add it as a whole token
                    if (i + 1 < chars.length && doubleOperators.matcher(Character.toString(chars[i + 1])).matches()) {
                        sb.append(chars[i + 1]);
                        i++;
                        col++;
                        tokenType = TokenType.getMatch(sb.toString());
                    }
                    if (tokenType != null)
                        ts.addNewToken(new Token(tokenType, null));
                } else {
                    // If it's not a token but it's a ! and the next character is a =, add it as a whole token
                    if (chars[i] == '!' && i + 1 < chars.length && chars[i + 1] == '=') {
                        i++;
                        col++;
                        sb.append(chars[i]);
                        tokenType = TokenType.getMatch(sb.toString());
                        if (tokenType != null)
                            ts.addNewToken(new Token(tokenType, null));
                    }
                }

                sb.setLength(0); //Clear buffer
            }
            else{
                sb.append(chars[i]);
            }

            if(chars[i] == '\n') col = 0;
        }


        //When everything ends, buffer length should be 0
        if(sb.length() > 0) errorFound(row, col - sb.length(), sb.toString());

        // Adding the EOF token
        TokenType tokenType = TokenType.getMatch("");
        if(tokenType != null)
            ts.addNewToken(new Token(tokenType, null));

        //Return the TokenStream or null if it's empty
        return ts.isEmpty()? null: ts;
    }

    private void errorFound(int row, int col, String token){
        System.out.printf(error, row, col, token);
    }
}
