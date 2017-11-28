package com.rmatushkin.service;

import com.rmatushkin.exception.FileException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.rmatushkin.util.StringUtil.removeExcessWhitespaces;
import static com.rmatushkin.util.StringUtil.removeUtf8Bom;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

public class FileService {

    public Map<String, String> collectNamesToUrls(String path) {
        File file = findFile(path);
        Map<String, String> namesToUrls = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String readLine = removeUtf8Bom(removeExcessWhitespaces(bufferedReader.readLine()));
                if (readLine.contains(" ")) {
                    String[] separatedLine = readLine.split(" ");
                    String url = separatedLine[0];
                    String name = separatedLine[1];
                    namesToUrls.put(name, url);
                }
                String name = now().toString();
                namesToUrls.put(name, readLine);
            }
            return namesToUrls;
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    }

    public File findFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileException(format("The path isn't exist: %s", path));
        }
        if (!file.canRead()) {
            throw new FileException(format("No access to read the path: %s", path));
        }
        if (!file.isFile()) {
            throw new FileException(format("It isn't the file path: %s", path));
        }
        return file;
    }

    public File createDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            return directory;
        }
        if (directory.mkdir()) {
            return directory;
        }
        throw new FileException(format("Can't create the directory by path: %s", path));
    }
}
