import de.nick.file.DefaultLanguageFile;
import de.nick.file.LanguageFile;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestFileWrite {

    @Test public void testWrite() {
        File fileDefault = new File("src/test/files/testDefault.langfile");
        fileDefault.getParentFile().mkdirs();
        File file = new File("src/test/files/test.langfile");
        Map<String, String> data = new LinkedHashMap<>();
        LanguageFile languageFile = LanguageFile.loadFromFile(file, StandardCharsets.UTF_8);
        languageFile.update(DefaultLanguageFile.loadFromFile(fileDefault, StandardCharsets.UTF_8));
        languageFile.save();
        System.out.println(languageFile.getMessage("name"));
    }

}
