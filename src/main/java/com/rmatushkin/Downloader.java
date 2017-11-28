package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.service.FileService;
import com.rmatushkin.service.ProgramArgumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private List<SingleFile> singleFiles;

    public Downloader(String[] args) {
        init(args);
    }

    public void start() {
        downloadFiles();
    }

    private void downloadFiles() {

    }

    private void init(String[] args) {
        ProgramArgumentService programArgumentService = new ProgramArgumentService();
        FileService fileService = new FileService();

        Map<String, String> parametersToValues = programArgumentService.parse(args, PARAMETERS);
        Map<String, String> namesToUrls = fileService.collectNamesToUrls(parametersToValues.get(PARAMETERS[2]));
        System.out.println(namesToUrls);
    }
}
