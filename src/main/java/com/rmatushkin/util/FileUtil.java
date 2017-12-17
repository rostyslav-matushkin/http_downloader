package com.rmatushkin.util;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.exception.FileException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rmatushkin.constraint.RegexPattern.FILE_EXTENSION;
import static com.rmatushkin.constraint.RegexPattern.URL_AND_FILE_NAME_REGEX;
import static com.rmatushkin.constraint.RegexPattern.URL_REGEX;
import static com.rmatushkin.util.StringUtil.removeAllWhitespaces;
import static com.rmatushkin.util.StringUtil.removeExcessWhitespaces;
import static com.rmatushkin.util.StringUtil.removeUtf8Bom;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

public class FileUtil {

    public static List<SingleFile> newSingleFiles(String filePath) {
        File file = findFile(filePath);
        List<SingleFile> singleFiles = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int count = 0;
            while (bufferedReader.ready()) {
                String readLine = processReadLine(bufferedReader.readLine());
                SingleFile singleFile = new SingleFile();
                if (readLine.contains(" ")) {
                    String[] urlAndFileName = readLine.split(" ");
                    singleFile.setUrl(urlAndFileName[0]);
                    singleFile.setFileName(urlAndFileName[1]);
                } else {
                    String fileExtension = extractFileExtension(readLine);
                    singleFile.setUrl(readLine);
                    String fileName = (++count) + fileExtension;
                    singleFile.setFileName(fileName);
                }
                singleFiles.add(singleFile);
            }
            return singleFiles;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new FileException(e);
        }
    }

    public static void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdir())
                throw new FileException(format("Can't create the directory by path: %s", path));
        }
    }

    private static File findFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileException(format("Path '%s' isn't exist", path));
        }
        if (!file.isFile()) {
            throw new FileException(format("Path '%s' isn't a file", path));
        }
        return file;
    }

    private static String processReadLine(String readLine) {
        readLine = removeUtf8Bom(readLine);
        validateUrl(readLine);

        if (readLine.matches(URL_AND_FILE_NAME_REGEX)) {
            return removeExcessWhitespaces(readLine);
        } else {
            return removeAllWhitespaces(readLine);
        }
    }

    private static void validateUrl(String string) {
        if (!string.matches(URL_REGEX)) {
            throw new FileException(format("Line '%s' isn't URL", string));
        }
    }

    private static String extractFileExtension(String string) {
        Pattern pattern = compile(FILE_EXTENSION);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
