package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.http.HttpClient;
import com.rmatushkin.service.FileService;
import com.rmatushkin.service.ProgramArgumentService;

import java.io.File;
import java.util.List;
import java.util.Map;

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
        Map<String, String> parametersToValues = programArgumentService.parse(args, PARAMETERS);

        FileService fileService = new FileService();
        singleFiles = fileService.collectSingleFiles(parametersToValues.get(PARAMETERS[2]));

        String destinationDirectoryPath = parametersToValues.get(PARAMETERS[3]);
        File destinationDirectory = fileService.createDirectory(destinationDirectoryPath);
        for (SingleFile singleFile : singleFiles) {
            singleFile.setDestinationDirectory(destinationDirectory);
        }
    }
}
