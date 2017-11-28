package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.exception.DownloaderException;
import com.rmatushkin.http.HttpClient;
import com.rmatushkin.service.FileService;
import com.rmatushkin.service.ProgramArgumentService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private List<SingleFile> singleFiles;

    public Downloader(String[] args) {
        init(args);
    }

    public void start() {
        HttpClient httpClient = new HttpClient();
        httpClient.download(singleFiles);
    }

    private void init(String[] args) {
        ProgramArgumentService programArgumentService = new ProgramArgumentService();
        FileService fileService = new FileService();

        Map<String, String> parametersToValues = programArgumentService.parse(args, PARAMETERS);
        Map<String, String> namesToUrls = fileService.collectNamesToUrls(parametersToValues.get(PARAMETERS[2]));
        String destinationDirectoryPath = parametersToValues.get(PARAMETERS[3]);
        File destinationDirectory = fileService.createDirectory(destinationDirectoryPath);
        singleFiles = new ArrayList<>(namesToUrls.size());

        for (Entry<String, String> pair : namesToUrls.entrySet()) {
            try {
                URL url = new URL(pair.getValue());
                String fileName = pair.getKey();

                SingleFile singleFile = new SingleFile();
                singleFile.setUrl(url);
                singleFile.setFileName(fileName);
                singleFile.setDestinationDirectory(destinationDirectory);

                singleFiles.add(singleFile);
            } catch (IOException e) {
                throw new DownloaderException(e.getMessage());
            }
        }

    }
}
