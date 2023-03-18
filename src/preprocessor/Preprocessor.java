package preprocessor;

import org.jetbrains.annotations.NotNull;

public interface Preprocessor {

    String generatePureHighLevelLanguage(@NotNull String filePath);
}
