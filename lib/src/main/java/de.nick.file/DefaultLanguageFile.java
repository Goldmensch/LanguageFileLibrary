package de.nick.file;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultLanguageFile {

    private final Map<String, String> data;

    private DefaultLanguageFile(Map<String, String> data) {
        this.data = data;
    }

    /**
     * Gets you a new DefaultLanguageFile with the data from the reader
     * @param reader must not be null
     * @return a new DefaultLanguageFile
     */
    public static DefaultLanguageFile loadFromReader(Reader reader) {
        Preconditions.checkNotNull(reader);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Map<String, String> data = new LinkedHashMap<>();
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                if(line.equalsIgnoreCase("")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                String key = line.split(": ", 2)[0];
                String value = line.split(": ", 2)[1];
                data.put(key, value);
                line = bufferedReader.readLine();
            }
            reader.close();
            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new DefaultLanguageFile(data);
    }

    /**
     * Gets you a new DefaultLanguageFile with the data from the InputStream
     * @param inputStream must not be null
     * @param charset the character set with which the InputStream should be loaded
     * @return a new DefaultLanguageFile
     */
    public static DefaultLanguageFile loadFromInputStream(InputStream inputStream, Charset charset) {
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        try {
            inputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return loadFromReader(inputStreamReader);
    }

    /**
     * Returns a new DefaultLanguageFile with the data from the file. If the file is not found, an empty DefaultLanguageFile is returned.
     * @param file must not be null
     * @param charset the character set with which the file should be loaded
     * @return a new DefaultLanguageFile
     */

    public static DefaultLanguageFile loadFromFile(File file, Charset charset){
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(file);
        try {
            return loadFromReader(Files.newReader(file, charset));
        } catch (FileNotFoundException e) {
            return new DefaultLanguageFile(new LinkedHashMap<>());
        }
    }

    /**
     * Gets you a new DefaultLanguageFile with the data from the Map
     * @param data must not be null
     * @return a new DefaultLanguageFile
     */
    public static DefaultLanguageFile loadFromMap(Map<String, String> data) {
        Preconditions.checkNotNull(data);
        return new DefaultLanguageFile(data);
    }

    /**
     * @return the data map
     */
    public Map<String, String> getData() {
        return data;
    }
}
