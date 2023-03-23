package preprocessor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class POPreprocessor implements Preprocessor {
    @Override
    @NotNull
    public String generatePureHighLevelLanguage(@NotNull String filePath) {
        return cleanFileContent(fileToString(filePath));
    }

    /**
     * This function is used to read all the file information and store it into one single line. The
     * bytes from the file are decoded into characters using the specified charset. The returned stream
     * contains the reference to an open file, and it is closed by closing the stream.
     *
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
            // TODO: Handle exception
            System.out.println("File not found");
        }

        return contentBuilder.toString();
    }

    /**
     * This function is used to clean the information read from the file, simulating a preprocessor
     * function so that the code is cleaned to be parsed or analized properly.
     *
     * @param content: String with the representation of the content of the file read before.
     * @return clearContent: String with the representation of the content of the file once cleaned.
     */
    private String cleanFileContent(String content){
        StringBuilder cleanContentBuilder = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '/' && content.charAt(i+1) == '/'){
                // CORRECT SHORT COMMENT
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
                    // CORRECT LONG COMMENT
                    i = i + 2; // We move to the begining of the comment

                    // Search for the closing of the comment
                    while(content.charAt(i) != '*' && content.charAt(i+1) != '/') {
                        // 2n case: Find chars "/*" but then the long comment is not closed
                        if (i > content.length()) break;
                        i++;
                    }
                    i = i + 2; // Jump both '*' and '/' characters
                } else {
                    // WRONG CODE -> Comment not formed correctly
                    // 1r case: Find char "/" but then the short comment is omitted
                    break;

                }
            }
            cleanContentBuilder.append(content.charAt(i));
        }
        return cleanContentBuilder.toString();
    }
}
