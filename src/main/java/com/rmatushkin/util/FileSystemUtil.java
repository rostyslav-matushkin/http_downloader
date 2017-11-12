package com.rmatushkin.util;

import com.rmatushkin.exception.FileSystemReaderException;

import java.io.File;

import static java.lang.String.format;

public class FileSystemUtil {

    public static File findFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileSystemReaderException(format("The path isn't exist: %s", path));
        }
        if (!file.canRead()) {
            throw new FileSystemReaderException(format("No access to read the path: %s", path));
        }
        if (!file.isFile()) {
            throw new FileSystemReaderException(format("It isn't the file path: %s", path));
        }
        return file;
    }

    public static File createDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            return directory;
        }
        if (directory.mkdir()) {
            return directory;
        }
        throw new FileSystemReaderException(format("Can't create the directory by path: %s", path));
    }
}
