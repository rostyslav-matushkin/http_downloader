package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.http.HttpClient;
import com.rmatushkin.service.FileService;
import com.rmatushkin.service.ProgramArgumentService;

import java.util.List;
import java.util.Map;

import static com.rmatushkin.http.Limit.parseLimit;
import static java.lang.Integer.parseInt;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private Map<String, String> parametersToValues;
    private List<SingleFile> singleFiles;

    public Downloader(String[] args) {
        init(args);
    }

    public void start() {
        HttpClient httpClient;
        String threadsQuantity = parametersToValues.get(PARAMETERS[0]);
        if (threadsQuantity == null) {
            httpClient = new HttpClient();
        } else {
            httpClient = new HttpClient(parseInt(threadsQuantity));
        }
        
        String limitData = parametersToValues.get(PARAMETERS[1]);
        if (limitData != null) {
            httpClient.setLimit(parseLimit(limitData));
        }

        httpClient.download(singleFiles);
    }

    private void init(String[] args) {
        ProgramArgumentService programArgumentService = new ProgramArgumentService();
        parametersToValues = programArgumentService.parse(args, PARAMETERS);

        FileService fileService = new FileService();
        String sourceFilePath = parametersToValues.get(PARAMETERS[2]);
        singleFiles = fileService.getSingleFiles(sourceFilePath);

        String destinationDirectoryPath = parametersToValues.get(PARAMETERS[3]);
        for (SingleFile singleFile : singleFiles) {
            singleFile.setDirectoryPath(destinationDirectoryPath);
        }
    }
}
