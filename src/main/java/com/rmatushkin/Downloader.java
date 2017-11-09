package com.rmatushkin;

import com.rmatushkin.exception.DownloaderException;
import com.rmatushkin.http.HttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.rmatushkin.util.FileSystemReader.createDirectory;
import static com.rmatushkin.util.FileSystemReader.findFile;
import static com.rmatushkin.util.ProgramArgumentParser.parse;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    public static final String UTF8_BOM = "\uFEFF";
    private String[] args;

    public Downloader(String[] args) {
        this.args = args;
    }

    public void start() {
        Map<String, String> valuesByParameters = parse(args, PARAMETERS);
        File foundFile = findFile(valuesByParameters.get(PARAMETERS[2]));
        File destinationDirectory = createDirectory(valuesByParameters.get(PARAMETERS[3]));
        Map<String, String> fileNamesByUrls = fetchFileNamesByUrls(foundFile);
        downloadFiles(fileNamesByUrls, destinationDirectory);
    }

    private Map<String, String> fetchFileNamesByUrls(File file) {
        Map<String, String> fileNamesByLinks = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String[] separatedLine = bufferedReader.readLine().replaceAll(UTF8_BOM, "").split(" ");
                fileNamesByLinks.put(separatedLine[1], separatedLine[0]);
            }
            return fileNamesByLinks;
        } catch (IOException e) {
            throw new DownloaderException(e.getMessage());
        }
    }

    private void downloadFiles(Map<String, String> fileNamesByLinks, File destinationDirectory) {
        HttpClient httpClient = new HttpClient();
        for (Entry<String, String> pair : fileNamesByLinks.entrySet()) {
            String fileName = pair.getKey();
            String url = pair.getValue();
            httpClient.downloadByUrl(url, fileName, destinationDirectory);
        }
    }
}
