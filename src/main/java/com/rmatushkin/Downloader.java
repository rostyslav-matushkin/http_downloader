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


}
