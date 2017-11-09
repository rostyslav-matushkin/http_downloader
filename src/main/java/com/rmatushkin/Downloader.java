package com.rmatushkin;

import com.rmatushkin.exception.DownloaderException;

import java.io.File;
import java.util.Map;

import static com.rmatushkin.util.ProgramArgumentParser.parse;
import static java.lang.String.format;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private String[] args;

    public Downloader(String[] args) {
        this.args = args;
    }

    public void start() {
        Map<String, String> valuesByParameters = parse(args, PARAMETERS);
        System.out.println(valuesByParameters);

        File fileWithLinks = findFileWithLinks(valuesByParameters.get(PARAMETERS[2]));
        File destinationDirectory = createDestinationDirectory(valuesByParameters.get(PARAMETERS[3]));

    }

    private File findFileWithLinks(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new DownloaderException(format("The path isn't exist: %s", path));
        }
        if (!file.canRead()) {
            throw new DownloaderException(format("Can't read the path: %s", path));
        }
        if (!file.isFile()) {
            throw new DownloaderException(format("It isn't the file by path: %s", path));
        }
        return file;
    }

    private File createDestinationDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            return directory;
        }
        if (directory.mkdir()) {
            return directory;
        }
        throw new DownloaderException(format("Can't create directory by path: %s", path));
    }
}
