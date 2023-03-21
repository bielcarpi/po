package preprocessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class POPreprocessor implements Preprocessor {
    @Override
    public @NotNull String generatePureHighLevelLanguage( @NotNull String filePath) {

        String content = fileToString(filePath);
        String cleanContent = cleanFileContent(content);

        return cleanContent;
    }

    /**
     * This function is used to read all the file information and store it into one single line. The
     * bytes from the file are decoded into characters using the specified charset. The returned stream
     * contains the reference to an open file, and it is closed by closing the stream.
     * @param filePath: Path of the file to read
     * @return fileContent: String with the representation of the content of the file
     */
    private String fileToString(String filePath){
        // String used to append all the file's information
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            // Stream of data appended from the file
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            // Handle exception
        }
        String fileContent = contentBuilder.toString();
        return fileContent;
    }

    /**
     * This function is used to clean the information read from the file, simulating a preprocessor
     * function so that the code is cleaned to be parsed or analized properly.
     * @param content: String with the representation of the content of the file read before.
     * @return clearContent: String with the representation of the content of the file once cleaned.
     */
    private String cleanFileContent(String content){
        StringBuilder cleanContentBuilder = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '/' && content.charAt(i+1) == '/'){
                // Short comment
                i = i + 2; // We move to the begining of the comment
                // Loop to skip all the content inside the comment
                while(content.charAt(i) != '\n') {
                    i++;
                }
                // Addition of the '\n' character to the string builder
                cleanContentBuilder.append(content.charAt(i));
                continue; // Back to the loop without adding another unusual char
            } else {
                if (content.charAt(i) == '/' && content.charAt(i+1) == '*'){
                    // Long comment
                    i = i + 2; // We move to the begining of the comment
                    while(content.charAt(i) != '*') {
                        i++;
                    }
                    i = i + 2; // Jump both '*' and '/' characters
                }
            }
            cleanContentBuilder.append(content.charAt(i));
        }

        return cleanContentBuilder.toString();
    }
}
