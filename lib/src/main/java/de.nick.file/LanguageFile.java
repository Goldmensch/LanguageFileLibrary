package de.nick.file;

import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class LanguageFile {

    private final Map<String, String> data;
    private final File file;

    private LanguageFile(Map<String, String> data, File file) {
        this.data = data;
        this.file = file;
    }

    /**
     * Loads a LanguageFile
     * @param charset the character set with which the file should be loaded
     * @param file must not be null
     * @return a new LanguageFile instance
     */
    public static LanguageFile loadFromFile(File file, Charset charset) {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        Map<String, String> data = new LinkedHashMap<>();
        if(!file.exists()) {
            return new LanguageFile(data, file);
        }
        try (BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), charset)) {
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
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return new LanguageFile(data, file);
    }

    /**
     * @return the data map
     */
    public Map<String, String> getMap() {
        return data;
    }

    /**
     * compares and updates this LanguageFile with a standard LanguageFile
     * @param defaultLanguageFile must not be null
     */
    public void update(DefaultLanguageFile defaultLanguageFile) {
        Preconditions.checkNotNull(defaultLanguageFile);
        Map<String, String> defaultData = defaultLanguageFile.getData();
        // add missing lines
        for (String current : defaultData.keySet()) {
            if (!data.containsKey(current)) {
                data.put(current, defaultData.get(current));
            }
        }
        // remove useless lines
        Set<Map.Entry<String, String>> removedEntrys = new HashSet<>();
        for (Map.Entry<String, String> current : data.entrySet()) {
            if (!defaultData.containsKey(current.getKey())) {
                removedEntrys.add(current);
            }
        }
        data.entrySet().removeAll(removedEntrys);
    }

    /**
     * saves the LanguageFile to disk
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> current : data.entrySet()) {
                writer.write(current.getKey() + ": " + current.getValue());
                writer.newLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @return the keyset
     */
    public Set<String> getKeys() {
        return data.keySet();
    }

    /**
     * reassembles the lines using the map.
     * @return a Stringlist with all line in the original order
     */
    public List<String> getLines() {
        List<String> lines = new LinkedList<>();
        for(Map.Entry<String, String> current : data.entrySet()) {
            lines.add(current.getKey() + ": " + current.getValue());
        }
        return lines;
    }

    /**
     * searches for the given path, if it is not found null is returned
     * @param path must be not null
     * @return the Message
     */
    public String getMessage(String path) {
        return data.get(path);
    }


}

